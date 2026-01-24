@echo off
setlocal EnableDelayedExpansion
title Iris License Tool

:: --- CONFIGURATION ---
set API_USER=admin
set API_PASS=velox@123
set API_URL=http://localhost:8081/api/license/generate
:: ---------------------

echo ========================================================
echo        IRIS LICENSE GENERATOR
echo ========================================================
echo.

:: ---------------------------------------------------------
:: STEP 1: CHECK FOR POM.XML (Location Check)
:: ---------------------------------------------------------
if not exist "pom.xml" (
    echo [ERROR] pom.xml not found!
    echo Please move this file to your Project Root folder.
    pause
    exit /b
)

:: ---------------------------------------------------------
:: STEP 2: FIND JAR FILE (Auto-Build if missing)
:: ---------------------------------------------------------
:FIND_JAR
set "JAR_FILE="
if exist "target\*.jar" (
    :: Find the first .jar file in target that isn't 'original-' (if maven creates those)
    for %%f in (target\*.jar) do (
        set "temp_name=%%~nxf"
        if "!temp_name:~0,9!" neq "original-" (
            set "JAR_FILE=target\%%f"
            goto :JAR_FOUND
        )
    )
)

if "%JAR_FILE%"=="" (
    echo [STATUS] Application JAR file not found.
    echo [INFO] Building project... (Please wait)
    call mvn clean package -DskipTests
    goto :FIND_JAR
)

:JAR_FOUND
echo [INFO] Found JAR: %JAR_FILE%

:: ---------------------------------------------------------
:: STEP 3: START SERVER IF NEEDED
:: ---------------------------------------------------------
netstat -an | find "8081" | find "LISTENING" >nul
if %errorlevel% equ 0 (
    echo [STATUS] Server is already running.
) else (
    echo [STATUS] Starting Server...

    :: Start Java in a new minimized window
    start "Iris Server" /MIN java -jar "%JAR_FILE%"

    echo [INFO] Waiting 20 seconds for startup...
    timeout /t 20 /nobreak >nul
)

:MENU
cls
echo ========================================================
echo        IRIS LICENSE GENERATOR (Ready)
echo ========================================================
echo.

set /p customerName="1. Customer Name      : "
set /p productName="2. Product Name       : "
set /p hwId="3. Hardware ID        : "
set /p maxUsers="4. Max Users          : "
set /p daysValid="5. Validity (Days)    : "

:: Calculate Dates using PowerShell
for /f %%i in ('powershell -command "(Get-Date).ToString('yyyy-MM-dd')"') do set issueDate=%%i
for /f %%i in ('powershell -command "(Get-Date).AddDays(%daysValid%).ToString('yyyy-MM-dd')"') do set expiryDate=%%i

echo.
echo Generating for %customerName%...

:: Create JSON Payload
(
echo {
echo   "licenseId": "LIC-%RANDOM%-%RANDOM%",
echo   "licenseName": "Commercial License",
echo   "description": "Generated via Tool",
echo   "productName": "%productName%",
echo   "customerName": "%customerName%",
echo   "hwId": "%hwId%",
echo   "issueDate": "%issueDate%",
echo   "expiryDate": "%expiryDate%",
echo   "maxUsers": %maxUsers%,
echo   "irisEnabled": true,
echo   "revoked": false
echo }
) > temp_req.json

:: Call API
curl -s -X POST %API_URL% ^
     -u "%API_USER%:%API_PASS%" ^
     -H "Content-Type: application/json" ^
     -d @temp_req.json ^
     --output "%customerName%_license.lic"

if exist "%customerName%_license.lic" (
    echo [SUCCESS] Saved: %customerName%_license.lic
) else (
    echo [ERROR] Failed. Server might not be ready yet.
)

del temp_req.json
echo.
set /p choice="Generate another? (Y/N): "
if /i "%choice%"=="Y" goto MENU

echo [INFO] Exiting.
pause