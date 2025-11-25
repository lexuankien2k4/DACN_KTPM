package com.Nhom7.DACN_KTPM.mapper;

import com.Nhom7.DACN_KTPM.dto.request.CreateConsultationRequest;
import com.Nhom7.DACN_KTPM.dto.response.ConsultationResponse;
import com.Nhom7.DACN_KTPM.entity.ConsultationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConsultationMapper {

    // --- MAP REQUEST (Đầu vào) ---
    // Bỏ qua các trường Entity phức tạp (sẽ set trong Service)
    @Mapping(target = "carVariant", ignore = true)
    @Mapping(target = "showroom", ignore = true)
    // Bỏ qua các trường hệ thống tự set
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ConsultationRequest toConsultationRequest(CreateConsultationRequest request);


    // --- MAP RESPONSE (Đầu ra) ---

    // 1. Tên phiên bản xe (VD: VF 8 Eco)
    @Mapping(source = "carVariant.name", target = "carVariantName")

    // 2. Tên dòng xe (VD: VF 8) - Cần trọc sâu vào carModel
    @Mapping(source = "carVariant.carModel.name", target = "carModelName")

    // 3. Tên Showroom
    @Mapping(source = "showroom.name", target = "showroomName")

    // 4. Địa chỉ Showroom (Mới thêm)
    @Mapping(source = "showroom.address", target = "showroomAddress")

    ConsultationResponse toConsultationResponse(ConsultationRequest consultationRequest);
}