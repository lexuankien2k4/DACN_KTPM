package com.Nhom7.DACN_KTPM.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "contracts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "contract_number", unique = true, nullable = false)
    String contractNumber;

    // Liên kết 1-1 với đơn đặt cọc
    @OneToOne
    @JoinColumn(name = "deposit_id", nullable = false)
    Deposit deposit;

    @Column(name = "final_price")
    BigDecimal finalPrice;

    @Column(name = "status")
    String status; // DRAFT, SIGNED, HANDOVER

    @Column(name = "created_by")
    String createdBy; // ID của Admin/Staff tạo hợp đồng

    @CreationTimestamp
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}