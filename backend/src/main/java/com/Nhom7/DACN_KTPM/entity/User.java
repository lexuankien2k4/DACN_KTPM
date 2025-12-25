package com.Nhom7.DACN_KTPM.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // <--- THÊM DÒNG NÀY
    private String id;

    @Column(name = "username", unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String username;

    String password;
    String firstName;
    LocalDate dob;
    String lastName;

    // 👇 BỔ SUNG THÊM 2 DÒNG NÀY ĐỂ HẾT LỖI 👇
    String phone;   // Lombok sẽ tự tạo hàm getPhone()
    String address; // Thêm luôn địa chỉ để dùng cho các chức năng khác
    // 👇 THÊM DÒNG NÀY VÀO:
    @Column(name = "customer_status") // Map với cột customer_status trong DB
    private String customerStatus;

    @ManyToMany
    Set<Role> roles;
}