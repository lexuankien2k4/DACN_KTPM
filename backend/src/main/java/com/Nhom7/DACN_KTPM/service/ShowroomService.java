package com.Nhom7.DACN_KTPM.service;

import com.Nhom7.DACN_KTPM.dto.response.ShowroomResponse;
import com.Nhom7.DACN_KTPM.entity.Consultation;
import com.Nhom7.DACN_KTPM.mapper.ShowroomMapper;
import com.Nhom7.DACN_KTPM.repository.ConsultationRequestRepository;
import com.Nhom7.DACN_KTPM.repository.ShowroomRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShowroomService {

    ShowroomRepository showroomRepository;
    ConsultationRequestRepository consultationRepository;
    ShowroomMapper showroomMapper;

    static final LocalTime START_TIME = LocalTime.of(9, 0);
    static final LocalTime END_TIME = LocalTime.of(17, 0);
    static final int SLOT_DURATION_MINUTES = 30;

    public List<String> getAllProvinces() {
        return showroomRepository.findDistinctProvinces();
    }

    // 👇 THÊM MỚI: Lấy tất cả showroom
    public List<ShowroomResponse> getAllShowrooms() {
        var showrooms = showroomRepository.findAll();
        return showroomMapper.toShowroomResponseList(showrooms);
    }

    public List<ShowroomResponse> getShowroomsByProvince(String province) {
        var showrooms = showroomRepository.findByProvince(province);
        return showroomMapper.toShowroomResponseList(showrooms);
    }

    public List<LocalTime> getShowroomAvailability(Integer showroomId, LocalDate date) {
        if (date.isBefore(LocalDate.now()) ||
                date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return List.of();
        }

        List<LocalTime> bookedSlots = consultationRepository
                .findByShowroomIdAndScheduledAtBetween(Math.toIntExact(Long.valueOf(showroomId)), date.atStartOfDay(), date.atTime(23, 59))
                .stream()
                .map(Consultation::getScheduledAt)
                .map(LocalDateTime::toLocalTime)
                .collect(Collectors.toList());

        return Stream.iterate(START_TIME, time -> time.plusMinutes(SLOT_DURATION_MINUTES))
                .limit((END_TIME.toSecondOfDay() - START_TIME.toSecondOfDay()) / (SLOT_DURATION_MINUTES * 60))
                .filter(slot -> !bookedSlots.contains(slot))
                .filter(slot -> !date.isEqual(LocalDate.now()) || slot.isAfter(LocalTime.now().plusHours(2)))
                .collect(Collectors.toList());
    }
}