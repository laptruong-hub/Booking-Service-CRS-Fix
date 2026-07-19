package com.crs.bookingservice.service;

import com.crs.bookingservice.dto.request.AiRecommendationRequest;
import com.crs.bookingservice.dto.response.AiRecommendationResponse;

public interface AiRecommendationService {

    AiRecommendationResponse recommendVehicle(AiRecommendationRequest request);
}