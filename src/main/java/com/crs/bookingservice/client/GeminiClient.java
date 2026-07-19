package com.crs.bookingservice.client;

import com.crs.bookingservice.config.GeminiConfig;
import com.crs.bookingservice.dto.gemini.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GeminiClient {

    private final RestTemplate restTemplate;
    private final GeminiConfig geminiConfig;

    public String generateContent(String prompt) {

        GeminiRequest geminiRequest =
                new GeminiRequest(
                        List.of(new Content(List.of(new Part(prompt))))
                );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GeminiRequest> entity = new HttpEntity<>(geminiRequest, headers);

        String url =
                "https://generativelanguage.googleapis.com/v1/models/"
                        + geminiConfig.getModel()
                        + ":generateContent?key="
                        + geminiConfig.getApiKey();

        ResponseEntity<GeminiResponse> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, GeminiResponse.class);

        return response.getBody()
                .getCandidates()
                .getFirst()
                .getContent()
                .getParts()
                .getFirst()
                .getText();
    }
}