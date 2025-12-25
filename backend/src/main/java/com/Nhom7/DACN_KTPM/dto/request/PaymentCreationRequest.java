package com.Nhom7.DACN_KTPM.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentCreationRequest {
    long amount;
    String userId;
    Long variantId;
    Integer showroomId;
    String selectedColor;
    String paymentMethod;
    String note;
    String orderInfo; // Thông tin hiển thị trên VNPAY (VD: "Đặt cọc xe VF3...")
}