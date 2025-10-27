package com.Nhom7.DACN_KTPM.mapper;

import com.Nhom7.DACN_KTPM.dto.request.CarModelCreationRequest;
import com.Nhom7.DACN_KTPM.dto.request.CarModelUpdateRequest;
import com.Nhom7.DACN_KTPM.dto.response.CarModelResponse;
import com.Nhom7.DACN_KTPM.entity.CarModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarModelMapper {

    @Mapping(source = "category.name", target = "categoryName")
    CarModelResponse toCarModelResponse(CarModel carModel); // Will map existing fields

    List<CarModelResponse> toCarModelResponseList(List<CarModel> carModels);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "variants", ignore = true)
    CarModel toCarModel(CarModelCreationRequest request);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "variants", ignore = true)
    void updateCarModel(@MappingTarget CarModel carModel, CarModelUpdateRequest request);
}
