package com.crs.bookingservice.client.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimulationRouteRequest {
    Double pickupLatitude;
    Double pickupLongitude;
    Double dropoffLatitude;
    Double dropoffLongitude;
}
