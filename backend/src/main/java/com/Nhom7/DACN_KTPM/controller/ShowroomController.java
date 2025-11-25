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
@RequestMapping("/api/showrooms")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShowroomController {

    ShowroomService showroomService;

    @GetMapping("/provinces")
    public ApiResponse<List<String>> getProvinces() {
        return ApiResponse.<List<String>>builder()
                .result(showroomService.getAllProvinces())
                .build();
    }

    @GetMapping
    public ApiResponse<List<ShowroomResponse>> getShowroomsByProvince(@RequestParam String province) {
        return ApiResponse.<List<ShowroomResponse>>builder()
                .result(showroomService.getShowroomsByProvince(province))
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