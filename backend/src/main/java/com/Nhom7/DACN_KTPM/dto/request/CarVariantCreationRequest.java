package com.Nhom7.DACN_KTPM.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CarVariantCreationRequest {

    @NotNull(message = "ID dòng xe không được để trống")
    private Integer modelId;

    @NotBlank(message = "Tên phiên bản không được để trống")
    private String name;

    @NotNull(message = "Giá xe không được để trống")
    @Positive(message = "Giá xe phải là số dương")
    private Long price;
    private Long discountedPrice;

    private Boolean isActive = true;

    // Các thông số kỹ thuật (có thể null nếu chưa có khi tạo)
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