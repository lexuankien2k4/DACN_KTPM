package com.Nhom7.DACN_KTPM.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepositResponse {
    Long id;
    BigDecimal amount;
    String status;
    String paymentMethod;

    // Thông tin chi tiết thay vì chỉ ID
    String userId;
    String customerName; // MỚI: Tên khách hàng
    String customerPhone; // MỚI: SĐT

    Long variantId;
    String carName;      // MỚI: Tên xe (VD: VinFast VF3)

    Integer showroomId;
    String showroomName; // MỚI: Tên showroom

    String selectedColor;
    String note;
    LocalDateTime createdAt;
}