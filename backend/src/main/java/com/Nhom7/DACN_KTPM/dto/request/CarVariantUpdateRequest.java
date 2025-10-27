package com.Nhom7.DACN_KTPM.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CarVariantUpdateRequest {
    // Không cho phép đổi modelId

    @NotBlank(message = "Tên phiên bản không được để trống")
    private String name;

    @Positive(message = "Giá xe phải là số dương")
    private Long price; // Cho phép cập nhật giá
    private Long discountedPrice;

    private Boolean isActive;

    // Tất cả thông số kỹ thuật cho phép cập nhật
    private Integer length;
    private Integer width;
    private Integer height;
    private Integer wheelbase;
    private Integer groundClearance;
    private Integer maxPower;
    private Integer maxTorque;
    private String drivetrain;
    private String driveModes;
    private BigDecimal battery;
    private Integer rangeNedc;
    private String chargeTimeDetail;
    private String suspensionSystem;
    private String brakeSystem;
    private Integer wheelSize;
    private String frontLightType;
    private String trunkAccess;
    private String airConditioningSystem;
    private BigDecimal entertainmentScreen;
    private String speakerSystem;
    private String driverSeatAdjustment;
}
