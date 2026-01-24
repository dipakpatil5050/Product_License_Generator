package com.velox.license.irislicenseserver.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LicenceResponse {
    private String product;
    private String customer;
    private String hwId;

    private LocalDate issueDate;
    private LocalDate expiryDate;

    private int  maxUsers;
    private boolean irisEnabled;
}

