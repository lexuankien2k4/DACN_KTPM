package com.Nhom7.DACN_KTPM.service;

import com.Nhom7.DACN_KTPM.dto.request.CreateConsultationRequest;
import com.Nhom7.DACN_KTPM.dto.request.UpdateConsultationStatusRequest;
import com.Nhom7.DACN_KTPM.dto.response.ConsultationResponse;
import com.Nhom7.DACN_KTPM.entity.CarVariant;
import com.Nhom7.DACN_KTPM.entity.Consultation;
import com.Nhom7.DACN_KTPM.entity.Showroom;
import com.Nhom7.DACN_KTPM.exception.AppException;
import com.Nhom7.DACN_KTPM.exception.ErrorCode;
import com.Nhom7.DACN_KTPM.mapper.ConsultationMapper;
import com.Nhom7.DACN_KTPM.repository.CarVariantRepository;
import com.Nhom7.DACN_KTPM.repository.ConsultationRequestRepository;
import com.Nhom7.DACN_KTPM.repository.ShowroomRepository;
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

    @Transactional
    public ConsultationResponse createConsultation(CreateConsultationRequest request) {
        Consultation entity = consultationMapper.toConsultationRequest(request);

        // request.getVariantId() bây giờ là Long, khớp với Repository
        if (request.getVariantId() != null) {
            CarVariant variant = carVariantRepository.findById(request.getVariantId())
                    .orElseThrow(() -> new AppException(ErrorCode.VARIANT_NOT_FOUND));
            entity.setCarVariant(variant);
        }

        Showroom showroom = null;
        if (request.getShowroomId() != null) {
            showroom = showroomRepository.findById(request.getShowroomId())
                    .orElseThrow(() -> new AppException(ErrorCode.SHOWROOM_NOT_FOUND));
            entity.setShowroom(showroom);
        }

        if (request.getScheduledAt() != null) {
            if (showroom == null) {
                throw new AppException(ErrorCode.SHOWROOM_REQUIRED_FOR_SCHEDULE);
            }
            entity.setStatus("Đã hẹn lịch");
        } else {
            entity.setStatus("Mới");
        }

        Consultation savedEntity = consultationRepository.save(entity);
        return consultationMapper.toConsultationResponse(savedEntity);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALES')")
    public List<ConsultationResponse> getAllConsultations(String status, String province) {
        return consultationRepository.findAll().stream()
                .map(consultationMapper::toConsultationResponse)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SALES')")
    @Transactional
    // 👇 SỬA: Đổi Integer requestId thành Long requestId
    public ConsultationResponse updateConsultationStatus(Long requestId, UpdateConsultationStatusRequest request) {
        Consultation entity = consultationRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));

        entity.setStatus(request.getNewStatus());
        Consultation updatedEntity = consultationRepository.save(entity);
        return consultationMapper.toConsultationResponse(updatedEntity);
    }
}