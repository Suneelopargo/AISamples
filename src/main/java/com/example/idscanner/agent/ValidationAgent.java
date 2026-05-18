package com.example.idscanner.agent;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.idscanner.dto.Claim;

@Component
public class ValidationAgent {

    public List<String> validate(Claim claim) {

        List<String> errors = new ArrayList<>();

        if (claim.getDocuments() == null || claim.getDocuments().isEmpty()) {
            errors.add("ERROR_MISSING_DOCUMENTS");
        }

        if (!Arrays.asList("B001", "B002", "B003")
                .contains(claim.getBillingCode())) {
            errors.add("ERROR_INVALID_BILLING_CODE");
        }

        if (claim.getPolicyNumber() == null ||
                !claim.getPolicyNumber().startsWith("POL")) {
            errors.add("ERROR_POLICY_MISMATCH");
        }

        return errors;
    }
}