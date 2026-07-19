package com.crs.bookingservice.service;

import com.crs.bookingservice.dto.request.AiRecommendationRequest;
import com.crs.bookingservice.dto.response.AiRecommendationResponse;
import com.crs.bookingservice.dto.request.AiChatRequest;
import com.crs.bookingservice.dto.response.AiChatResponse;

public interface AiRecommendationService {

    AiRecommendationResponse recommendVehicle(AiRecommendationRequest request);
    
    AiChatResponse chat(AiChatRequest request);
}