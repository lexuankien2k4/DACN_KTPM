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

    // 👇 THÊM DÒNG NÀY: Bỏ qua trường carModels khi map từ Request sang Entity
    @Mapping(target = "carModels", ignore = true)
    CarCategory toCarCategory(CarCategoryCreationRequest request);

    CarCategoryResponse toCarCategoryResponse(CarCategory carCategory);

    // 👇 THÊM DÒNG NÀY: Bỏ qua trường carModels khi update
    @Mapping(target = "carModels", ignore = true)
    void updateCarCategory(@MappingTarget CarCategory carCategory, CarCategoryUpdateRequest request);

    List<CarCategoryResponse> toCarCategoryResponseList(List<CarCategory> all);
}