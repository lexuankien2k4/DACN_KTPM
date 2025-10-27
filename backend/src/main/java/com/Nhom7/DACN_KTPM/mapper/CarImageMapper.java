package com.Nhom7.DACN_KTPM.mapper;

import com.Nhom7.DACN_KTPM.dto.response.CarImageResponse;
import com.Nhom7.DACN_KTPM.entity.CarImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarImageMapper {
    CarImageResponse toCarImageResponse(CarImage carImage);
}