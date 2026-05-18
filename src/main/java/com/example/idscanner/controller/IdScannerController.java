package com.example.idscanner.controller;

import com.example.idscanner.dto.IdCardResponse;
import com.example.idscanner.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/id")
@CrossOrigin(origins = "*")
public class IdScannerController {

    @Autowired
    private OpenAIService openAIService;

    @PostMapping("/scan")
    public ResponseEntity<IdCardResponse> scan(
            @RequestParam("file") MultipartFile file
    ) {

        try {

            IdCardResponse response = openAIService.scanIdCard(file);

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.internalServerError().build();
        }
    }
}