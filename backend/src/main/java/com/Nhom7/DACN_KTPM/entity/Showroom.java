package com.Nhom7.DACN_KTPM.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "showrooms")
public class Showroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name; // Ví dụ: "VinFast Times City"

    @Column(name = "address", nullable = false)
    private String address; // Địa chỉ chi tiết (ví dụ: "458 Minh Khai, Hai Bà Trưng")

    @Column(name = "province", nullable = false)
    private String province; // Tỉnh/Thành (ví dụ: "Hà Nội")

    @Column(name = "district")
    private String district; // Quận/Huyện (ví dụ: "Hai Bà Trưng")
}