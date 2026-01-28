package com.velox.license.irislicenseserver.service;

import com.velox.license.irislicenseserver.DTO.LicenceResponse;
import com.velox.license.irislicenseserver.model.License;
import com.velox.license.irislicenseserver.repository.LicenseRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LicenseService {

    private final LicenseRepository repo;
    private final LicenseSigner signer;

    private final ObjectMapper mapper = new ObjectMapper();

    public byte[] generateLicense(License entity) throws Exception{

        entity.setIssueDate(LocalDate.now());

        LicenceResponse payload = new LicenceResponse();
        payload.setProduct(entity.getProductName());
        payload.setCustomer(entity.getCustomerName());
        payload.setHwId(entity.getHwId());
        payload.setIssueDate(entity.getIssueDate());
        payload.setExpiryDate(entity.getExpiryDate());
        payload.setMaxUsers(entity.getMaxUsers());

        byte[] payloadBytes = mapper.writeValueAsBytes(payload);
        byte[] signature = signer.sign(payloadBytes);

        repo.save(entity);
        return Base64.getEncoder().encode(
                (new String(payloadBytes) +
                        "\n--SIGN--\n" +
                        Base64.getEncoder().encodeToString(signature))
                        .getBytes()
        );
    }

    public List<License> getAllLicenseRecords() {
        return repo.findAll();
    }

    public void removeAllLicenses() {
        repo.deleteAll();
    }

    public void removeLicenseById(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
        }
    }
}
