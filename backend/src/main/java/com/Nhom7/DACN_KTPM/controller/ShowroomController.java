package com.Nhom7.DACN_KTPM.controller;

import com.Nhom7.DACN_KTPM.dto.request.ApiResponse;
import com.Nhom7.DACN_KTPM.dto.response.ShowroomResponse;
import com.Nhom7.DACN_KTPM.service.ShowroomService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
// 👇 SỬA ĐƯỜNG DẪN: Khớp với fetch('http://localhost:8080/api/public/showrooms') ở Frontend
@RequestMapping("/api/public/showrooms")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShowroomController {

    ShowroomService showroomService;

    // API lấy danh sách tỉnh (Dùng cho dropdown nếu muốn fetch riêng)
    @GetMapping("/provinces")
    public ApiResponse<List<String>> getProvinces() {
        return ApiResponse.<List<String>>builder()
                .result(showroomService.getAllProvinces())
                .build();
    }

    // 👇 SỬA LOGIC: Province không bắt buộc (required = false)
    // Nếu không truyền province, trả về TOÀN BỘ showroom để Frontend tự lọc
    @GetMapping
    public ApiResponse<List<ShowroomResponse>> getShowrooms(@RequestParam(required = false) String province) {
        List<ShowroomResponse> result;
        if (province != null && !province.isEmpty()) {
            result = showroomService.getShowroomsByProvince(province);
        } else {
            result = showroomService.getAllShowrooms(); // Lấy tất cả
        }

        return ApiResponse.<List<ShowroomResponse>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/{id}/availability")
    public ApiResponse<List<LocalTime>> getShowroomAvailability(
            @PathVariable("id") Integer showroomId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return ApiResponse.<List<LocalTime>>builder()
                .result(showroomService.getShowroomAvailability(showroomId, date))
                .build();
    }
}