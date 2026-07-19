package com.crs.bookingservice.util;

import com.crs.bookingservice.client.dto.VehicleDto;
import com.crs.bookingservice.dto.request.AiRecommendationRequest;
import com.crs.bookingservice.dto.request.AiChatRequest;

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
You are an expert electric vehicle rental consultant and travel planner in Vietnam.

Task 1: Recommend UP TO TWO vehicles from the list of AVAILABLE VEHICLES below.
Do NOT recommend any vehicle that is not in this list.
IMPORTANT: All vehicles are electric. Consider battery level vs trip distance —
do NOT recommend a vehicle with insufficient battery for the requested distance.

Task 2: Suggest 3-5 Points of Interest (POIs) along the route from Origin to Destination.
Types of POIs can be FOOD, CAFE, SCENERY.
Provide concise and engaging descriptions for each POI (max 4-5 sentences in Vietnamese).

Available Vehicles:
%s

Customer Information
Origin: %s
Destination: %s
Purpose: %s
Passengers: %d
Distance: %.1f km
Terrain: %s
Self Drive: %s

Return ONLY valid JSON with this exact structure:
{
    "recommendedVehicles": [
        {
            "plateNumber": "",
            "reason": "Detailed but concise reasoning in Vietnamese (max 4-5 sentences)"
        }
    ],
    "pois": [
        {
            "name": "Name of place",
            "type": "FOOD or CAFE or SCENERY",
            "description": "Engaging description in Vietnamese (max 4-5 sentences)"
        }
    ]
}

Do NOT use markdown.
Do NOT use ```json.
Do NOT explain anything else.
""".formatted(
                vehicleListText,
                request.getOrigin(),
                request.getDestination(),
                request.getPurpose(),
                request.getPassengers(),
                request.getDistance(),
                request.getTerrain(),
                request.isSelfDrive() ? "Yes" : "No"
        );
    }

    public static String buildChatPrompt(AiChatRequest request) {
        return """
You are a friendly and knowledgeable AI travel assistant in Vietnam.
You are helping a customer plan their trip. 
The current context/destination of the user's trip is: %s

The customer asks: "%s"

Provide a helpful, engaging, and detailed answer in Vietnamese. 
Keep it concise enough for a chat interface (under 150 words).
Do NOT use markdown (like ** or #).
""".formatted(
            request.getContext() != null ? request.getContext() : "Vietnam",
            request.getMessage()
        );
    }
}