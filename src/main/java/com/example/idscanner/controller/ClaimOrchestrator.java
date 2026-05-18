package com.example.idscanner.controller;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.idscanner.agent.ExceptionAgent;
import com.example.idscanner.agent.IngestionAgent;
import com.example.idscanner.agent.ValidationAgent;
import com.example.idscanner.dto.Claim;

@Service
public class ClaimOrchestrator {

    private final IngestionAgent ingestion;
    private final ValidationAgent validation;
    private final ExceptionAgent exception;

    public ClaimOrchestrator(IngestionAgent ingestion,
                             ValidationAgent validation,
                             ExceptionAgent exception) {
        this.ingestion = ingestion;
        this.validation = validation;
        this.exception = exception;
    }

    public Map<String, Object> processClaim(Claim claim) {

        Claim data = ingestion.ingest(claim);

        List<String> errors = validation.validate(data);

        Map<String, Object> response = new HashMap<>();

        if (errors.isEmpty()) {
            response.put("status", "PASS");
        } else {
            response.put("status", "FAIL");
            response.put("errors", errors);
            response.put("explanations", exception.explain(errors));
        }

        return response;
    }
}