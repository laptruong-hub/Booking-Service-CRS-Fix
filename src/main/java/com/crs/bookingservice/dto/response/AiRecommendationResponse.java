package com.crs.bookingservice.dto.response;

import com.crs.bookingservice.dto.gemini.GeminiPoi;
import com.crs.bookingservice.dto.gemini.GeminiVehicleRecommendation;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class AiRecommendationResponse {
    private List<GeminiVehicleRecommendation> recommendedVehicles;
    private List<GeminiPoi> pois;
}