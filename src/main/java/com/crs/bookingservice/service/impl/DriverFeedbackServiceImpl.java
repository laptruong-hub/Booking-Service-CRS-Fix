package com.crs.bookingservice.service.impl;

import com.crs.bookingservice.dto.request.CreateFeedbackRequest;
import com.crs.bookingservice.dto.response.DriverFeedbackResponse;
import com.crs.bookingservice.entity.DriverFeedback;
import com.crs.bookingservice.entity.DriverProfile;
import com.crs.bookingservice.entity.RentalUnit;
import com.crs.bookingservice.exception.InvalidRequestException;
import com.crs.bookingservice.exception.ResourceNotFoundException;
import com.crs.bookingservice.repository.DriverFeedbackRepository;
import com.crs.bookingservice.repository.DriverProfileRepository;
import com.crs.bookingservice.repository.RentalUnitRepository;
import com.crs.bookingservice.service.DriverFeedbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverFeedbackServiceImpl implements DriverFeedbackService {

    private final DriverFeedbackRepository driverFeedbackRepository;
    private final DriverProfileRepository driverProfileRepository;
    private final RentalUnitRepository rentalUnitRepository;

    @Override
    @Transactional
    public DriverFeedbackResponse createFeedback(CreateFeedbackRequest request) {
        log.info("REST request to create feedback for RentalUnit #{} and Driver #{}", request.getRentalUnitId(), request.getDriverId());

        if (driverFeedbackRepository.existsByRentalUnitId(request.getRentalUnitId())) {
            throw new InvalidRequestException("Đã tồn tại đánh giá cho chuyến xe này.");
        }

        RentalUnit rentalUnit = rentalUnitRepository.findById(request.getRentalUnitId())
                .orElseThrow(() -> new ResourceNotFoundException("RentalUnit", request.getRentalUnitId()));

        DriverProfile driver = driverProfileRepository.findById(request.getDriverId())
                .orElseThrow(() -> new ResourceNotFoundException("DriverProfile", request.getDriverId()));

        DriverFeedback feedback = DriverFeedback.builder()
                .rentalUnit(rentalUnit)
                .driver(driver)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        feedback = driverFeedbackRepository.save(feedback);

        // Update Driver's average rating
        Double newAverage = driverFeedbackRepository.getAverageRatingByDriverId(driver.getId());
        driver.setAverageRating(newAverage);
        driverProfileRepository.save(driver);

        log.info("Feedback created successfully for Driver #{}. New Average: {}", driver.getId(), newAverage);

        return DriverFeedbackResponse.builder()
                .id(feedback.getId())
                .rentalUnitId(rentalUnit.getId())
                .driverId(driver.getId())
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .createdAt(feedback.getCreatedAt())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DriverFeedbackResponse> getDriverFeedbacks(Long driverId) {
        log.info("Fetching feedbacks for Driver #{}", driverId);
        
        // Ensure driver exists
        if (!driverProfileRepository.existsById(driverId)) {
            throw new ResourceNotFoundException("DriverProfile", driverId);
        }

        List<DriverFeedback> feedbacks = driverFeedbackRepository.findByDriverId(driverId);
        
        // Sort by newest first
        feedbacks.sort((f1, f2) -> f2.getCreatedAt().compareTo(f1.getCreatedAt()));

        return feedbacks.stream().map(feedback -> DriverFeedbackResponse.builder()
                .id(feedback.getId())
                .rentalUnitId(feedback.getRentalUnit().getId())
                .driverId(feedback.getDriver().getId())
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .createdAt(feedback.getCreatedAt())
                .build()).collect(Collectors.toList());
    }
}
