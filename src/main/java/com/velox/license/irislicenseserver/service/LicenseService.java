package com.velox.license.irislicenseserver.service;

import com.velox.license.irislicenseserver.DTO.LicenceResponse;
import com.velox.license.irislicenseserver.model.License;
import com.velox.license.irislicenseserver.repository.LicenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class LicenseService {

    private final LicenseRepository repo;
    private final LicenseSigner signer;

    private final ObjectMapper mapper = new ObjectMapper();

    public byte[] generateLicense(License entity) throws Exception{

        entity.setIssueDate(LocalDate.now());
        repo.save(entity);

        LicenceResponse payload = new LicenceResponse();
        payload.setProduct(entity.getProductName());
        payload.setCustomer(entity.getCustomerName());
        payload.setHwId(entity.getHwId());
        payload.setIssueDate(entity.getIssueDate());
        payload.setExpiryDate(entity.getExpiryDate());
        payload.setMaxUsers(entity.getMaxUsers());
        payload.setIrisEnabled(entity.isIrisEnabled());

        byte[] payloadBytes = mapper.writeValueAsBytes(payload);


        byte[] signature = signer.sign(payloadBytes);

        return Base64.getEncoder().encode(
                (new String(payloadBytes) +
                        "\n--SIGN--\n" +
                        Base64.getEncoder().encodeToString(signature))
                        .getBytes()
        );
    }
}
