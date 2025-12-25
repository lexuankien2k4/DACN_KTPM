package com.Nhom7.DACN_KTPM.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import com.Nhom7.DACN_KTPM.dto.response.UserResponse;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsultationResponse {
    Integer id;

    String customerName;
    String phoneNumber;
    String email;
    String province;
    String note;

    // Trạng thái & Lịch hẹn
    String status;
    LocalDateTime scheduledAt;
    LocalDateTime createdAt;



    String carVariantName;
    String carModelName;

    String showroomName;
    String showroomAddress;
    UserResponse staff;
}