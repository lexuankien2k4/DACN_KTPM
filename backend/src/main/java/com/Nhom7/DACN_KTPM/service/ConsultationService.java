package com.Nhom7.DACN_KTPM.service;

import com.Nhom7.DACN_KTPM.dto.request.CreateConsultationRequest;
import com.Nhom7.DACN_KTPM.dto.request.UpdateConsultationStatusRequest;
import com.Nhom7.DACN_KTPM.dto.response.ConsultationResponse;
import com.Nhom7.DACN_KTPM.entity.CarVariant;
import com.Nhom7.DACN_KTPM.entity.ConsultationRequest;
import com.Nhom7.DACN_KTPM.entity.Showroom;
import com.Nhom7.DACN_KTPM.exception.AppException;
import com.Nhom7.DACN_KTPM.exception.ErrorCode;
import com.Nhom7.DACN_KTPM.mapper.ConsultationMapper;
import com.Nhom7.DACN_KTPM.repository.CarVariantRepository;
import com.Nhom7.DACN_KTPM.repository.ConsultationRequestRepository;
import com.Nhom7.DACN_KTPM.repository.ShowroomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConsultationService {

    ConsultationRequestRepository consultationRepository;
    CarVariantRepository carVariantRepository;
    ShowroomRepository showroomRepository;
    ConsultationMapper consultationMapper;
    ObjectMapper objectMapper;

    @Transactional
    public ConsultationResponse createConsultation(CreateConsultationRequest request) {
        // 1. Dùng Mapper để chuyển đổi cơ bản
        ConsultationRequest entity = consultationMapper.toConsultationRequest(request);

        // 2. Tìm và gán các Entity liên quan
        CarVariant variant = carVariantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_FOUND));
        entity.setCarVariant(variant);

        Showroom showroom = null;
        if (request.getShowroomId() != null) {
            showroom = showroomRepository.findById(request.getShowroomId())
                    .orElseThrow(() -> new AppException(ErrorCode.SHOWROOM_NOT_FOUND));
            entity.setShowroom(showroom);
        }

        // 3. Xử lý logic nghiệp vụ
        if (request.getScheduledAt() != null) {
            if (showroom == null) {
                throw new AppException(ErrorCode.SHOWROOM_REQUIRED_FOR_SCHEDULE);
            }
            boolean isSlotTaken = consultationRepository
                    .findByShowroomIdAndScheduledAt(request.getShowroomId(), request.getScheduledAt())
                    .isPresent();
            if (isSlotTaken) {
                throw new AppException(ErrorCode.SCHEDULE_SLOT_TAKEN);
            }
            entity.setStatus("Đã hẹn lịch");
        } else {
            entity.setStatus("Mới");
        }

        // 4. Xử lý trường JSON
        if (request.getSelectedLoanDetails() != null) {
            try {
                entity.setSelectedLoanDetails(objectMapper.writeValueAsString(request.getSelectedLoanDetails()));
            } catch (Exception e) {
                throw new AppException(ErrorCode.JSON_PROCESSING_ERROR);
            }
        }

        // 5. Lưu và trả về DTO
        ConsultationRequest savedEntity = consultationRepository.save(entity);
        return consultationMapper.toConsultationResponse(savedEntity);
    }

    // == CÁC API CỦA ADMIN ==

    @PreAuthorize("hasAnyRole('ADMIN', 'SALES')")
    public List<ConsultationResponse> getAllConsultations(String status, String province) {
        // (Thêm logic lọc dựa trên status, province nếu cần)
        return consultationRepository.findAll().stream()
                .map(consultationMapper::toConsultationResponse)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALES')")
    @Transactional
    public ConsultationResponse updateConsultationStatus(Integer requestId, UpdateConsultationStatusRequest request) {
        ConsultationRequest entity = consultationRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));

        entity.setStatus(request.getNewStatus());
        ConsultationRequest updatedEntity = consultationRepository.save(entity);
        return consultationMapper.toConsultationResponse(updatedEntity);
    }
}