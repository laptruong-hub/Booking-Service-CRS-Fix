package com.crs.bookingservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVehicleRequest {
    private String color;
    private String status;
    private Double odometerKm;
    private Long fleetHubId;
    private String currentBookingId;
    private String currentDriverId;
}
