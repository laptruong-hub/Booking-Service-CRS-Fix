package com.crs.bookingservice.util;

import com.crs.bookingservice.client.dto.VehicleDto;
import com.crs.bookingservice.dto.request.AiRecommendationRequest;

import java.util.List;
import java.util.stream.Collectors;

public class PromptBuilder {

    public static String buildVehicleRecommendationPrompt(
            AiRecommendationRequest request,
            List<VehicleDto> availableVehicles) {

        String vehicleListText = availableVehicles.stream()
                .map(v -> "- %s %s (plate: %s, battery: %d%%, odometer: %.1f km, hub: %s)".formatted(
                        v.getBrand(),
                        v.getModelName(),
                        v.getPlateNumber(),
                        v.getCurrentState() != null ? v.getCurrentState().getBatteryLevel() : -1,
                        v.getOdometerKm(),
                        v.getFleetHubName()
                ))
                .collect(Collectors.joining("\n"));

        return """
You are an expert electric vehicle rental consultant.

Recommend ONE vehicle ONLY from the list of AVAILABLE VEHICLES below.
Do NOT recommend any vehicle that is not in this list.
IMPORTANT: All vehicles are electric. Consider battery level vs trip distance —
do NOT recommend a vehicle with insufficient battery for the requested distance.

Available Vehicles:
%s

Customer Information

Purpose: %s

Passengers: %d

Distance: %.1f km

Terrain: %s

Self Drive: %s

Return ONLY valid JSON, using the exact plate number as the identifier.

{
    "recommendedVehicle":"",
    "reason":""
}

Do NOT use markdown.

Do NOT use ```json.

Do NOT explain anything else.

""".formatted(
                vehicleListText,
                request.getPurpose(),
                request.getPassengers(),
                request.getDistance(),
                request.getTerrain(),
                request.isSelfDrive() ? "Yes" : "No"
        );
    }
}