package com.velox.license.irislicenseserver.controller;

import com.velox.license.irislicenseserver.model.License;
import com.velox.license.irislicenseserver.service.LicenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
