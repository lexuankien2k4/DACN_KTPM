package com.Nhom7.DACN_KTPM.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class CarVariantDetailResponse {
    private Long id;
    private String modelName; // Tên dòng xe
    private String name; // Tên phiên bản
    private Long price;
    private Long discountedPrice;
    private Boolean isActive;

    private List<CarImageResponse> images; // Danh sách ảnh

    // Thông số kỹ thuật
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
