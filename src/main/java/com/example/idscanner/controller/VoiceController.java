package com.example.idscanner.controller;


import com.example.idscanner.dto.IdCardResponse;
import com.example.idscanner.dto.VoiceRequest;
import com.example.idscanner.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/voice")
@CrossOrigin(origins = "*")
public class VoiceController {

    @Autowired
    private OpenAIService openAIService;

    @PostMapping("/extract")
    public IdCardResponse extractVoiceData(
            @RequestBody VoiceRequest request
    ) throws Exception {

        return openAIService.extractVoiceDetails(
                request.getTranscript()
        );
    }
}