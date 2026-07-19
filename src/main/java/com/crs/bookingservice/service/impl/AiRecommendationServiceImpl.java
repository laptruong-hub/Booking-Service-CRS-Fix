package com.crs.bookingservice.service.impl;

import com.crs.bookingservice.client.CarManagementClient;
import com.crs.bookingservice.client.GeminiClient;
import com.crs.bookingservice.client.dto.CarManagementApiResponse;
import com.crs.bookingservice.client.dto.VehicleDto;
import com.crs.bookingservice.dto.gemini.GeminiVehicleResult;
import com.crs.bookingservice.dto.request.AiRecommendationRequest;
import com.crs.bookingservice.dto.response.AiRecommendationResponse;
import com.crs.bookingservice.service.AiRecommendationService;
import com.crs.bookingservice.util.PromptBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiRecommendationServiceImpl implements AiRecommendationService {

    private final CarManagementClient carManagementClient;
    private final GeminiClient geminiClient;
    private final ObjectMapper objectMapper;

    @Override
    public AiRecommendationResponse recommendVehicle(AiRecommendationRequest request) {

        List<VehicleDto> availableVehicles = fetchAvailableVehicles();

        if (availableVehicles.isEmpty()) {
            return AiRecommendationResponse.builder()
                    .recommendedVehicle("Unknown")
                    .reason("No available vehicles at the moment.")
                    .build();
        }

        String prompt = PromptBuilder.buildVehicleRecommendationPrompt(request, availableVehicles);

        String result;
        try {
            result = geminiClient.generateContent(prompt);
        } catch (Exception e) {
            log.error("Gemini API call failed", e);
            return fallbackResponse(availableVehicles);
        }

        try {
            result = result.replace("```json", "").replace("```", "").trim();

            GeminiVehicleResult aiResult =
                    objectMapper.readValue(result, GeminiVehicleResult.class);

            boolean isValid = availableVehicles.stream()
                    .anyMatch(v -> v.getPlateNumber().equalsIgnoreCase(aiResult.getRecommendedVehicle()));

            if (!isValid) {
                log.warn("Gemini recommended a plate not in available list: {}",
                        aiResult.getRecommendedVehicle());
                return fallbackResponse(availableVehicles);
            }

            return AiRecommendationResponse.builder()
                    .recommendedVehicle(aiResult.getRecommendedVehicle())
                    .reason(aiResult.getReason())
                    .build();

        } catch (Exception e) {
            log.error("Failed to parse Gemini response: {}", result, e);
            return fallbackResponse(availableVehicles);
        }
    }

    private List<VehicleDto> fetchAvailableVehicles() {
        try {
            CarManagementApiResponse<List<VehicleDto>> response =
                    carManagementClient.getVehiclesByStatus("AVAILABLE");

            if (response == null || response.getData() == null) {
                return Collections.emptyList();
            }
            return response.getData();

        } catch (Exception e) {
            log.error("Failed to fetch available vehicles from car-management-service", e);
            return Collections.emptyList();
        }
    }

    private AiRecommendationResponse fallbackResponse(List<VehicleDto> availableVehicles) {
        VehicleDto fallback = availableVehicles.get(0);
        return AiRecommendationResponse.builder()
                .recommendedVehicle(fallback.getPlateNumber())
                .reason("AI recommendation unavailable, showing first available vehicle.")
                .build();
    }
}