package com.crs.bookingservice.dto.gemini;

import lombok.Data;
import java.util.List;

@Data
public class GeminiTripResult {
    private List<GeminiVehicleRecommendation> recommendedVehicles;
    private List<GeminiPoi> pois;
}
