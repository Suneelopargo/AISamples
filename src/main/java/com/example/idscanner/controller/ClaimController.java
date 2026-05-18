package com.example.idscanner.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.idscanner.dto.Claim;

@RestController
@RequestMapping("/api/claims")
@CrossOrigin
public class ClaimController {

    private final ClaimOrchestrator orchestrator;

    public ClaimController(ClaimOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping("/process")
    public Map<String, Object> process(@RequestBody Claim claim) {
        return orchestrator.processClaim(claim);
    }
}