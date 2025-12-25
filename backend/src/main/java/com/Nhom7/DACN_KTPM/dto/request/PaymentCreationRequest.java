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
    String fullName;
    String phone;
    String email;
    String citizenId; // CCCD

    Long variantId;
    Integer showroomId;
    String selectedColor;
    String paymentMethod;
    String note;
    String orderInfo;
}