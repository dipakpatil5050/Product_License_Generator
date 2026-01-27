package com.velox.license.irislicenseserver.controller;

import com.velox.license.irislicenseserver.model.License;
import com.velox.license.irislicenseserver.service.LicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/license")
@RequiredArgsConstructor
public class LicenseController {

    private final LicenseService licenseService;

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateLicense(@RequestBody License licenseRequest) throws Exception{

        byte[] licenseFile = licenseService.generateLicense(licenseRequest);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=iris_license.lic")
                .body(licenseFile);
    }

    @GetMapping("/records")
    public ResponseEntity<List<License>> getAllLicenseRecords() throws Exception{
        return ResponseEntity.ok(licenseService.getAllLicenseRecords());
    }


    @DeleteMapping("/removeAll")
    public ResponseEntity<String> removeAllLicenseRecords() throws Exception{
        licenseService.removeAllLicenses();
        return ResponseEntity.ok("All Licenses Deleted Successfully!!!");
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeLicenseById(@PathVariable(name = "id") Long id) throws Exception{
        licenseService.removeLicenseById(id);
        return ResponseEntity.ok("License Deleted Successfully");
    }

}
