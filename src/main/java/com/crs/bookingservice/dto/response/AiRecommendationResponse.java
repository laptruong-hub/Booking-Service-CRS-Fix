package com.crs.bookingservice.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AiRecommendationResponse {

    private String recommendedVehicle;

    private String reason;

}