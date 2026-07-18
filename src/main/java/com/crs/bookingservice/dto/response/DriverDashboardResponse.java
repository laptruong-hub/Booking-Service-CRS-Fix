package com.crs.bookingservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverDashboardResponse {
    Double rating;
    Long totalRating;
    Long totalTrips;
    Long completedTrips;
    BigDecimal thisMonthEarnings;
    BigDecimal totalEarnings;
    Double acceptanceRate;
    Double cancellationRate;
}
