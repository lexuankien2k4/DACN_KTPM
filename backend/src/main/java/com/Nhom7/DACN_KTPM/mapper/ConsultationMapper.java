package com.Nhom7.DACN_KTPM.mapper;

import com.Nhom7.DACN_KTPM.dto.request.CreateConsultationRequest;
import com.Nhom7.DACN_KTPM.dto.response.ConsultationResponse;
import com.Nhom7.DACN_KTPM.entity.ConsultationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ConsultationMapper {

    // Bỏ qua các trường Entity (vì chúng ta cần tìm & set thủ công trong Service)
    @Mapping(target = "carVariant", ignore = true)
    @Mapping(target = "showroom", ignore = true)
    // Bỏ qua các trường sẽ được set logic
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "selectedLoanDetails", ignore = true)
    ConsultationRequest toConsultationRequest(CreateConsultationRequest request);


    // Map các trường lồng ghép để trả về Response
    @Mapping(source = "carVariant.name", target = "carVariantName")
    @Mapping(source = "showroom.name", target = "showroomName")
    ConsultationResponse toConsultationResponse(ConsultationRequest consultationRequest);
}
