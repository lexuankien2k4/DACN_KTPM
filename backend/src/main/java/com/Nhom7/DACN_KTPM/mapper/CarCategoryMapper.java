package com.Nhom7.DACN_KTPM.mapper;

import com.Nhom7.DACN_KTPM.dto.request.CarCategoryCreationRequest;
import com.Nhom7.DACN_KTPM.dto.request.CarCategoryUpdateRequest;
import com.Nhom7.DACN_KTPM.dto.response.CarCategoryResponse;
import com.Nhom7.DACN_KTPM.entity.CarCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarCategoryMapper {

    // Map Entity sang Response DTO
    CarCategoryResponse toCarCategoryResponse(CarCategory category);

    // Map danh sách Entity sang danh sách Response DTO
    List<CarCategoryResponse> toCarCategoryResponseList(List<CarCategory> categories);

    // Map Creation Request DTO sang Entity (bỏ qua id)
    @Mapping(target = "id", ignore = true)
    CarCategory toCarCategory(CarCategoryCreationRequest request);

    // Cập nhật Entity hiện có từ Update Request DTO (bỏ qua id)
    @Mapping(target = "id", ignore = true)
    void updateCarCategory(@MappingTarget CarCategory category, CarCategoryUpdateRequest request);
}
