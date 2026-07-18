package com.crs.bookingservice.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DriverFeedbackResponse {

    Long id;
    Long rentalUnitId;
    Long driverId;
    Integer rating;
    String comment;
    LocalDateTime createdAt;
}
