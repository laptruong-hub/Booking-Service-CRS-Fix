package com.crs.bookingservice.dto.gemini;

import lombok.Data;

@Data
public class GeminiVehicleResult {

    private String recommendedVehicle;

    private String reason;

}