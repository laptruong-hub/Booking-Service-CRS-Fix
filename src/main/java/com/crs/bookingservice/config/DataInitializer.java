package com.crs.bookingservice.config;

import com.crs.bookingservice.entity.PaymentMethod;
import com.crs.bookingservice.entity.DriverProfile;
import com.crs.bookingservice.enums.DriverStatus;
import com.crs.bookingservice.enums.PaymentMethodType;
import com.crs.bookingservice.repository.PaymentMethodRepository;
import com.crs.bookingservice.repository.DriverProfileRepository;
import com.crs.bookingservice.client.IamServiceClient;
import com.crs.bookingservice.client.dto.IamUserDto;
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
    private final IamServiceClient iamServiceClient;

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
            log.info("Đang chờ Iam-Service để lấy UUID của Driver mặc định...");
            String driverId = null;
            for (int i = 0; i < 20; i++) { // Retry up to 20 times (max 60 seconds)
                try {
                    List<IamUserDto> drivers = iamServiceClient.getUsersByRole("DRIVER");
                    if (drivers != null && !drivers.isEmpty()) {
                        driverId = drivers.get(0).getUserId();
                        break;
                    }
                } catch (Exception e) {
                    // Ignore exception, iam-service might still be booting
                }
                try {
                    Thread.sleep(3000); // Wait 3 seconds before retrying
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (driverId != null) {
                log.info("Khởi tạo hồ sơ tài xế mẫu với ID: {}", driverId);
                DriverProfile profile = DriverProfile.builder()
                        .userId(driverId)
                        .licenseNumber("DRV-99999")
                        .currentLocation("Ho Chi Minh City")
                        .status(DriverStatus.ACTIVE)
                        .averageRating(5.0)
                        .build();
                driverProfileRepository.save(profile);
                log.info("Đã tạo hồ sơ cho tài xế mặc định.");
            } else {
                log.warn("KHÔNG thể lấy UUID của driver từ IAM-Service. DriverProfile chưa được tạo.");
            }
        }
    }
}
