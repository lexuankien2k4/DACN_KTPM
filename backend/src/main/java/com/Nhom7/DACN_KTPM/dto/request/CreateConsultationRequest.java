package com.Nhom7.DACN_KTPM.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateConsultationRequest {
    @NotBlank(message = "Tên khách hàng không được để trống")
    String customerName;

    @NotBlank(message = "Số điện thoại không được để trống")
    String phoneNumber;

    String province;

    @NotNull(message = "ID phiên bản xe không được để trống")
    Integer variantId;

    Map<String, Object> selectedLoanDetails;

    Integer showroomId;

    @Future(message = "Thời gian hẹn phải là thời điểm trong tương lai")
    LocalDateTime scheduledAt;
}
