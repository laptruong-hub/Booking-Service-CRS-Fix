package com.crs.bookingservice.service.impl;

import com.crs.bookingservice.client.GeminiClient;
import com.crs.bookingservice.dto.gemini.DamageAssessmentRequest;
import com.crs.bookingservice.dto.gemini.DamageAssessmentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiDamageAssessmentService {

    private final GeminiClient geminiClient;
    private final ObjectMapper objectMapper;

    public DamageAssessmentResponse analyzeDamage(DamageAssessmentRequest request) {
        String base64Image = request.getBase64Image();
        String mimeType = request.getMimeType();

        // Strip prefix if exists, e.g. "data:image/jpeg;base64,"
        if (base64Image != null && base64Image.contains(",")) {
            String[] parts = base64Image.split(",");
            base64Image = parts[1];
            if (mimeType == null || mimeType.isEmpty()) {
                // Extract mime type from prefix
                String prefix = parts[0]; // data:image/jpeg;base64
                mimeType = prefix.replace("data:", "").replace(";base64", "");
            }
        }

        if (mimeType == null || mimeType.isEmpty()) {
            mimeType = "image/jpeg"; // Default
        }

        String prompt = buildPrompt();

        try {
            String result = geminiClient.generateContentWithImage(prompt, mimeType, base64Image);

            // Clean up Markdown formatting from the response
            result = result.replace("```json", "")
                    .replace("```", "")
                    .trim();

            return objectMapper.readValue(result, DamageAssessmentResponse.class);

        } catch (Exception e) {
            log.error("Gemini AI Image Analysis failed", e);
            return fallbackResponse();
        }
    }

    private String buildPrompt() {
        return """
                You are an expert car damage assessor. Analyze the provided image of a car.
                Detect any visible damages such as scratches, dents, or cracks.
                
                For each damage, provide:
                1. type: A short description of the damage (e.g., "Xước nhẹ", "Móp lớn") in Vietnamese.
                2. location: The location on the car (e.g., "Cản trước", "Cửa sau bên trái") in Vietnamese.
                3. severity: The severity level: "LOW", "MEDIUM", or "HIGH".
                4. coords: The relative coordinates of the damage bounding box on the image (x, y, width, height in percentage from 0 to 100).
                
                If there is any damage, set hasDamage to true.
                Set confidence to a number between 0 and 100 indicating your confidence level.
                
                Return ONLY a valid JSON object with the exact structure below:
                {
                    "hasDamage": true,
                    "damages": [
                        {
                            "id": 1,
                            "type": "Xước nhẹ",
                            "location": "Cản trước",
                            "severity": "LOW",
                            "coords": {
                                "x": 10,
                                "y": 20,
                                "width": 5,
                                "height": 5
                            }
                        }
                    ],
                    "confidence": 95
                }
                """;
    }

    private DamageAssessmentResponse fallbackResponse() {
        return DamageAssessmentResponse.builder()
                .hasDamage(false)
                .damages(java.util.List.of())
                .confidence(0)
                .build();
    }
}
