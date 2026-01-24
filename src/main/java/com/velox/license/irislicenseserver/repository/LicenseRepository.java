package com.velox.license.irislicenseserver.repository;

import com.velox.license.irislicenseserver.model.License;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenseRepository extends JpaRepository<License, Long> {
}
