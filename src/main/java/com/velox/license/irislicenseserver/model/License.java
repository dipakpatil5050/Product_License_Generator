package com.velox.license.irislicenseserver.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class License {
    @Id
    @Column(name = "id", columnDefinition = "INTEGER")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
