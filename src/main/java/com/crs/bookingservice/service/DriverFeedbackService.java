package com.crs.bookingservice.service;

import com.crs.bookingservice.dto.request.CreateFeedbackRequest;
import com.crs.bookingservice.dto.response.DriverFeedbackResponse;

import java.util.List;

public interface DriverFeedbackService {
    DriverFeedbackResponse createFeedback(CreateFeedbackRequest request);

    List<DriverFeedbackResponse> getDriverFeedbacks(Long driverId);
}
