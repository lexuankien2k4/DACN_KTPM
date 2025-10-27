package com.Nhom7.DACN_KTPM.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "car_variants")
public class CarVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private CarModel carModel;

    @OneToMany(mappedBy = "carVariant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CarImage> images;

    @Column(name = "name", nullable = false)
    private String name; // tên biến thể

    @Column(name = "price", nullable = false)
    private Long price;
    @Column(name = "discounted_price",nullable = true)
    private Long discountedPrice;
    @Column(name = "is_active")
    private Boolean isActive;
    // == Kích thước ==
    @Column(name = "length")
    private Integer length;
    @Column(name = "width")
    private Integer width;
    @Column(name = "height")
    private Integer height;
    @Column(name = "wheelbase")
    private Integer wheelbase;
    @Column(name = "ground_clearance")
    private Integer groundClearance;

    // == Hiệu suất ==
    @Column(name = "max_power")
    private Integer maxPower;
    @Column(name = "max_torque")
    private Integer maxTorque;
    @Column(name = "drivetrain")
    private String drivetrain;
    @Column(name = "drive_modes")
    private String driveModes;

    // == Pin & Sạc ==
    @Column(name = "battery")
    private BigDecimal battery;
    @Column(name = "range_nedc")
    private Integer rangeNedc;
    @Column(name = "charge_time_detail")
    private String chargeTimeDetail;

    // == Các thông tin khác ==
    @Column(name = "suspension_system")
    private String suspensionSystem;
    @Column(name = "brake_system")
    private String brakeSystem;
    @Column(name = "wheel_size")
    private Integer wheelSize;
    @Column(name = "front_light_type")
    private String frontLightType;
    @Column(name = "trunk_access")
    private String trunkAccess;
    @Column(name = "air_conditioning_system")
    private String airConditioningSystem;
    @Column(name = "entertainment_screen")
    private BigDecimal entertainmentScreen;
    @Column(name = "speaker_system")
    private String speakerSystem;
    @Column(name = "driver_seat_adjustment")
    private String driverSeatAdjustment;


}
