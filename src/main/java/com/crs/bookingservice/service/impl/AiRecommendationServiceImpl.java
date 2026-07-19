package com.crs.bookingservice.service.impl;

import com.crs.bookingservice.client.CarManagementClient;
import com.crs.bookingservice.client.GeminiClient;
import com.crs.bookingservice.client.dto.CarManagementApiResponse;
import com.crs.bookingservice.client.dto.VehicleDto;
import com.crs.bookingservice.dto.gemini.GeminiTripResult;
import com.crs.bookingservice.dto.gemini.GeminiVehicleRecommendation;
import com.crs.bookingservice.dto.gemini.GeminiPoi;
import com.crs.bookingservice.dto.request.AiRecommendationRequest;
import com.crs.bookingservice.dto.response.AiRecommendationResponse;
import com.crs.bookingservice.dto.request.AiChatRequest;
import com.crs.bookingservice.dto.response.AiChatResponse;
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
            log.warn("No available vehicles to recommend.");
            return AiRecommendationResponse.builder()
                    .recommendedVehicles(Collections.emptyList())
                    .pois(Collections.emptyList())
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

            GeminiTripResult aiResult = objectMapper.readValue(result, GeminiTripResult.class);

            // Filter out recommended vehicles that are not actually in the available list
            List<GeminiVehicleRecommendation> validRecommendations = aiResult.getRecommendedVehicles().stream()
                    .filter(rec -> availableVehicles.stream().anyMatch(v -> v.getPlateNumber().equalsIgnoreCase(rec.getPlateNumber())))
                    .toList();

            if (validRecommendations.isEmpty()) {
                log.warn("Gemini recommended plates not in available list.");
                return fallbackResponse(availableVehicles);
            }

            return AiRecommendationResponse.builder()
                    .recommendedVehicles(validRecommendations)
                    .pois(aiResult.getPois())
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
        
        GeminiVehicleRecommendation rec = new GeminiVehicleRecommendation();
        rec.setPlateNumber(fallback.getPlateNumber());
        rec.setReason("AI recommendation unavailable, showing first available vehicle.");
        
        GeminiPoi poi = new GeminiPoi();
        poi.setName("Trạm dừng chân");
        poi.setType("CAFE");
        poi.setDescription("Gợi ý mặc định do lỗi hệ thống AI.");

        return AiRecommendationResponse.builder()
                .recommendedVehicles(List.of(rec))
                .pois(List.of(poi))
                .build();
    }

    @Override
    public AiChatResponse chat(AiChatRequest request) {
        String prompt = PromptBuilder.buildChatPrompt(request);
        String reply;
        try {
            reply = geminiClient.generateContent(prompt);
            // Clean up Markdown if any
            reply = reply.replace("**", "").replace("##", "").replace("#", "");
        } catch (Exception e) {
            log.error("Gemini API call failed for chat", e);
            reply = "Xin lỗi, hiện tại hệ thống AI đang quá tải. Vui lòng thử lại sau.";
        }
        return AiChatResponse.builder().reply(reply).build();
    }
}