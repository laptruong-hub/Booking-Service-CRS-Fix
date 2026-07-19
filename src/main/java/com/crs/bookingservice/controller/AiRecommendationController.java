package com.crs.bookingservice.controller;

import com.crs.bookingservice.dto.request.AiRecommendationRequest;
import com.crs.bookingservice.dto.response.AiRecommendationResponse;
import com.crs.bookingservice.service.AiRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiRecommendationController {

    private final AiRecommendationService service;

    @PostMapping("/recommend")
    public AiRecommendationResponse recommend(
            @RequestBody AiRecommendationRequest request){

        return service.recommendVehicle(request);

    }

}