package com.crs.bookingservice.config;

import com.crs.bookingservice.entity.PaymentMethod;
import com.crs.bookingservice.entity.DriverProfile;
import com.crs.bookingservice.enums.DriverStatus;
import com.crs.bookingservice.enums.PaymentMethodType;
import com.crs.bookingservice.repository.PaymentMethodRepository;
import com.crs.bookingservice.repository.DriverProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * DataInitializer — khởi tạo dữ liệu mẫu ban đầu.
 * Chỉ chạy khi bảng trống (idempotent).
 */
@Component
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("null")
public class DataInitializer implements CommandLineRunner {

    private final PaymentMethodRepository paymentMethodRepository;
    private final DriverProfileRepository driverProfileRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (paymentMethodRepository.count() == 0) {
            log.info("Khởi tạo dữ liệu mẫu cho Booking Service...");

            List<PaymentMethod> methods = List.of(
                    PaymentMethod.builder()
                            .methodType(PaymentMethodType.CASH)
                            .displayName("Tiền mặt")
                            .isActive(true)
                            .build(),
                    PaymentMethod.builder()
                            .methodType(PaymentMethodType.BANK_TRANSFER)
                            .displayName("Chuyển khoản ngân hàng")
                            .isActive(true)
                            .build(),
                    PaymentMethod.builder()
                            .methodType(PaymentMethodType.CREDIT_CARD)
                            .displayName("Thẻ tín dụng")
                            .isActive(true)
                            .build(),
                    PaymentMethod.builder()
                            .methodType(PaymentMethodType.E_WALLET)
                            .displayName("Ví điện tử (Momo/ZaloPay)")
                            .isActive(true)
                            .build());

            paymentMethodRepository.saveAll(methods);
            log.info("Đã khởi tạo {} phương thức thanh toán.", methods.size());
        }

        if (driverProfileRepository.count() == 0) {
            log.info("Khởi tạo hồ sơ tài xế mẫu...");
            DriverProfile profile = DriverProfile.builder()
                    .userId("d0000000-0000-0000-0000-000000000001") // Match the UUID from iam-service
                    .licenseNumber("DRV-99999")
                    .currentLocation("Ho Chi Minh City")
                    .status(DriverStatus.ACTIVE)
                    .averageRating(5.0)
                    .build();
            driverProfileRepository.save(profile);
            log.info("Đã tạo hồ sơ cho tài xế mặc định.");
        }
    }
}
