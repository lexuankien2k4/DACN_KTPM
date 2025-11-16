package com.Nhom7.DACN_KTPM.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsultationResponse {
    Integer id;
    String customerName;
    String phoneNumber;
    String province;
    String status;
    LocalDateTime scheduledAt;

    // Chi tiết gói vay (trả về dưới dạng chuỗi JSON)
    String selectedLoanDetails;

    // Thông tin lồng ghép từ các bảng liên quan
    String carVariantName;
    String showroomName;

}