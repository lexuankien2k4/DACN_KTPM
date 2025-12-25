package com.Nhom7.DACN_KTPM.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "deposits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Deposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // 👇 THAY ĐỔI: Map trực tiếp sang Entity User
    @ManyToOne(fetch = FetchType.EAGER) // Hoặc LAZY tùy nhu cầu tối ưu
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    // 👇 THAY ĐỔI: Map sang CarVariant
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "variant_id", nullable = false)
    CarVariant variant;

    // 👇 THAY ĐỔI: Map sang Showroom
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "showroom_id")
    Showroom showroom;

    @Column(name = "selected_color")
    String selectedColor;

    BigDecimal amount;

    String status;

    @Column(name = "payment_method")
    String paymentMethod;

    @Column(name = "staff_id")
    String staffId; // Có thể nâng cấp thành User staff sau nếu cần

    @Column(columnDefinition = "TEXT")
    String note;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}