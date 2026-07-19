package com.crs.bookingservice.controller;

import com.crs.bookingservice.dto.request.AiRecommendationRequest;
import com.crs.bookingservice.dto.response.AiRecommendationResponse;
import com.crs.bookingservice.dto.request.AiChatRequest;
import com.crs.bookingservice.dto.response.AiChatResponse;
import com.crs.bookingservice.dto.gemini.DamageAssessmentRequest;
import com.crs.bookingservice.dto.gemini.DamageAssessmentResponse;
import com.crs.bookingservice.service.AiRecommendationService;
import com.crs.bookingservice.service.impl.AiDamageAssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AiRecommendationController {

    private final AiRecommendationService service;
    private final AiDamageAssessmentService damageService;

    @PostMapping("/recommend")
    public AiRecommendationResponse recommend(
            @RequestBody AiRecommendationRequest request){
        return service.recommendVehicle(request);
    }

    @PostMapping("/analyze-damage")
    public DamageAssessmentResponse analyzeDamage(
            @RequestBody DamageAssessmentRequest request){
        return damageService.analyzeDamage(request);
    }

    @PostMapping("/chat")
    public AiChatResponse chat(@RequestBody AiChatRequest request) {
        return service.chat(request);
    }
}