package com.Nhom7.DACN_KTPM.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateConsultationRequest {

    @JsonProperty("customer_name")
    @NotBlank(message = "Tên khách hàng không được để trống")
    private String customerName;

    @JsonProperty("phone_number")
    @NotBlank(message = "Số điện thoại không được để trống")
    private String phoneNumber;

    private String email;
    private String province;


    @JsonProperty("variant_id")
    private Long variantId;


    @JsonProperty("showroom_id")
    private Integer showroomId;

    @JsonProperty("scheduled_at")
    private LocalDateTime scheduledAt;

    private String note;
}