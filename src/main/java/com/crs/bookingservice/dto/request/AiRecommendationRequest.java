package com.crs.bookingservice.dto.request;

import lombok.Data;

@Data
public class AiRecommendationRequest {

    private String purpose;

    private int passengers;

    private double distance;

    private String terrain;

    private boolean selfDrive;

    private String origin;

    private String destination;

}