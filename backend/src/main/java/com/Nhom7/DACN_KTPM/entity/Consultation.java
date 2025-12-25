package com.Nhom7.DACN_KTPM.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import com.Nhom7.DACN_KTPM.entity.User;
@Data
@Entity
@Table(name = "consultation_requests") // Khớp tên bảng trong SQL
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // SQL: bigint -> Java: Long

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "province")
    private String province;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "status")
    private String status;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    // --- QUAN TRỌNG: KHỚP VỚI DATABASE ---

    // SQL: showroom_id int -> Java liên kết với Entity Showroom có ID Integer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showroom_id")
    private Showroom showroom;

    // SQL: variant_id bigint -> Java liên kết với Entity CarVariant có ID Long
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    private CarVariant carVariant;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp // Tự động lấy giờ hiện tại khi lưu
    private LocalDateTime createdAt;
    // --- BỔ SUNG THÊM ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id") // Tên cột trong DB sẽ là staff_id
    private User staff;
}