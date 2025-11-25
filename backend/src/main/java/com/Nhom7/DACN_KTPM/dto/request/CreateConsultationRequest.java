package com.Nhom7.DACN_KTPM.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateConsultationRequest {

    @NotBlank(message = "Tên khách hàng không được để trống")
    String customerName;

    @NotBlank(message = "Số điện thoại không được để trống")
    String phoneNumber;

    String email;
    String province;
    String note;

    Integer variantId;

    Integer showroomId;

    @Future(message = "Thời gian hẹn phải là một thời điểm trong tương lai")
    LocalDateTime scheduledAt;
}