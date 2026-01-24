package com.velox.license.irislicenseserver.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String licenseId;
    private String licenseName;
    private String description;
    private String productName;
    private String customerName;
    private String hwId;

    private LocalDate issueDate;
    private LocalDate expiryDate;

    private int  maxUsers;
    private boolean irisEnabled;

    private boolean revoked;




}
