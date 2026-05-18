package com.example.idscanner.service;

import com.example.idscanner.dto.IdCardResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    public IdCardResponse scanIdCard(MultipartFile file) throws Exception {

        String base64Image = Base64.getEncoder().encodeToString(file.getBytes());

		/*
		 * String prompt = """ Extract the following details from this ID card image.
		 * Return only JSON.
		 * 
		 * { \"fullName\": \"\", \"dob\": \"\", \"gender\": \"\", \"address\": \"\",
		 * \"idNumber\": \"\" } """;
		 */
        
        String prompt = """
                Extract the following details from this ID card image.

                Return ONLY valid JSON.

                Rules:
                - Split address into structured fields
                - Extract city, state, pincode, country separately
                - Extract phone number if available
                - If field not available return empty string
                - Do not return markdown

                {
                  "fullName": "",
                  "dob": "",
                  "gender": "",
                  "phoneNumber": "",
                  "addressLine1": "",
                  "city": "",
                  "state": "",
                  "pincode": "",
                  "country": "",
                  "idNumber": ""
                }
                """;

        Map<String, Object> imageUrl = Map.of(
                "url",
                "data:image/jpeg;base64," + base64Image
        );

        Map<String, Object> textContent = Map.of(
                "type", "text",
                "text", prompt
        );

        Map<String, Object> imageContent = Map.of(
                "type", "image_url",
                "image_url", imageUrl
        );

        Map<String, Object> message = Map.of(
                "role", "user",
                "content", List.of(textContent, imageContent)
        );

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4.1-mini",
                "messages", List.of(message),
                "max_tokens", 500
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.openai.com/v1/chat/completions",
                HttpMethod.POST,
                entity,
                String.class
        );

        ObjectMapper mapper = new ObjectMapper();

        JsonNode root = mapper.readTree(response.getBody());

        String content = root
                .get("choices")
                .get(0)
                .get("message")
                .get("content")
                .asText();

        content = content.replace("```json", "")
                .replace("```", "")
                .trim();

        return mapper.readValue(content, IdCardResponse.class);
    }
    
    public IdCardResponse extractVoiceDetails(
            String transcript
    ) throws Exception {

		/*
		 * String prompt = """ Extract the following details from the voice transcript.
		 * 
		 * Return ONLY valid JSON.
		 * 
		 * { "fullName": "", "dob": "", "gender": "", "address": "", "idNumber": "" }
		 * 
		 * Transcript: """ + transcript;
		 */
    	
    	String prompt = """
    	        Extract the following details
    	        from the voice transcript.

    	        Return ONLY valid JSON.

    	        Rules:
    	        - Split address into:
    	          - addressLine1
    	          - city
    	          - state
    	          - pincode
    	          - country
    	        - Extract phone number if available
    	        - If field unavailable return empty string
    	        - Do not return markdown

    	        {
    	          "fullName": "",
    	          "dob": "",
    	          "gender": "",
    	          "phoneNumber": "",
    	          "addressLine1": "",
    	          "city": "",
    	          "state": "",
    	          "pincode": "",
    	          "country": "",
    	          "idNumber": ""
    	        }

    	        Transcript:
    	        """ + transcript;

        Map<String, Object> message = Map.of(
                "role", "user",
                "content", prompt
        );

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4.1-mini",
                "messages", List.of(message),
                "max_tokens", 300
        );

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response =
                restTemplate.exchange(
                        "https://api.openai.com/v1/chat/completions",
                        HttpMethod.POST,
                        entity,
                        String.class
                );

        ObjectMapper mapper = new ObjectMapper();

        JsonNode root =
                mapper.readTree(response.getBody());

        String content = root
                .get("choices")
                .get(0)
                .get("message")
                .get("content")
                .asText();

        content = content
                .replace("```json", "")
                .replace("```", "")
                .trim();

        return mapper.readValue(
                content,
                IdCardResponse.class
        );
    }
}