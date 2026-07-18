package com.crs.bookingservice.repository;

import com.crs.bookingservice.entity.DriverFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverFeedbackRepository extends JpaRepository<DriverFeedback, Long> {

    List<DriverFeedback> findByDriverId(Long driverId);

    List<DriverFeedback> findByRentalUnitId(Long rentalUnitId);

    @Query("SELECT COALESCE(AVG(f.rating), 0.0) FROM DriverFeedback f WHERE f.driver.id = :driverId")
    Double getAverageRatingByDriverId(@Param("driverId") Long driverId);

    @Query("SELECT COUNT(f) FROM DriverFeedback f WHERE f.driver.id = :driverId")
    Long countFeedbackByDriverId(@Param("driverId") Long driverId);
}
