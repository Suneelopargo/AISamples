package com.example.idscanner.agent;


import org.springframework.stereotype.Component;

import com.example.idscanner.dto.Claim;

@Component
public class IngestionAgent {

    public Claim ingest(Claim claim) {
        System.out.println("✅ Ingestion Agent: Claim received " + claim.getClaimId());
        return claim;
    }
}
