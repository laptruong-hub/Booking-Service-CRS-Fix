package com.crs.bookingservice.controller;

import com.crs.bookingservice.dto.request.CreateFeedbackRequest;
import com.crs.bookingservice.dto.response.ApiResponse;
import com.crs.bookingservice.dto.response.DriverFeedbackResponse;
import com.crs.bookingservice.service.DriverFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Feedback", description = "API quản lý đánh giá của khách hàng đối với tài xế")
public class DriverFeedbackController {

    private final DriverFeedbackService driverFeedbackService;

    @PostMapping("/driver")
    @Operation(summary = "[CUSTOMER] Đánh giá tài xế", description = "Gửi điểm số và nhận xét cho tài xế sau khi hoàn thành chuyến đi.")
    public ResponseEntity<ApiResponse<DriverFeedbackResponse>> createFeedback(
            @Valid @RequestBody CreateFeedbackRequest request) {
        DriverFeedbackResponse response = driverFeedbackService.createFeedback(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Cảm ơn bạn đã gửi đánh giá!"));
    }

    @GetMapping("/driver/{driverId}")
    @Operation(summary = "[DRIVER] Xem đánh giá", description = "Lấy danh sách các đánh giá của khách hàng đối với một tài xế cụ thể.")
    public ResponseEntity<ApiResponse<List<DriverFeedbackResponse>>> getDriverFeedbacks(
            @PathVariable Long driverId) {
        List<DriverFeedbackResponse> responses = driverFeedbackService.getDriverFeedbacks(driverId);
        return ResponseEntity.ok(ApiResponse.success(responses, "Lấy danh sách đánh giá thành công!"));
    }
}
