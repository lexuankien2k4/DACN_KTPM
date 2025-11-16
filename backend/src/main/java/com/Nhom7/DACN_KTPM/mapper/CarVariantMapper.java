package com.Nhom7.DACN_KTPM.mapper;

import com.Nhom7.DACN_KTPM.dto.request.CarVariantCreationRequest;
import com.Nhom7.DACN_KTPM.dto.request.CarVariantUpdateRequest;
import com.Nhom7.DACN_KTPM.dto.response.CarVariantBasicResponse;
import com.Nhom7.DACN_KTPM.dto.response.CarVariantDetailResponse;
import com.Nhom7.DACN_KTPM.entity.CarImage;
import com.Nhom7.DACN_KTPM.entity.CarVariant;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CarImageMapper.class})
public interface CarVariantMapper {



    @Mapping(source = "carModel.name", target = "modelName")
    @Mapping(target = "firstImageUrl", expression = "java(getFirstImageUrl(carVariant.getImages()))") // Lấy ảnh đầu tiên
    CarVariantBasicResponse toCarVariantBasicResponse(CarVariant carVariant);

    List<CarVariantBasicResponse> toCarVariantBasicResponseList(List<CarVariant> carVariants);

    @Mapping(source = "carModel.name", target = "modelName")
    CarVariantDetailResponse toCarVariantDetailResponse(CarVariant carVariant);




    @Mapping(target = "id", ignore = true)
    @Mapping(target = "carModel", ignore = true) // Sẽ gán carModel trong service
    @Mapping(target = "images", ignore = true)

    CarVariant toCarVariant(CarVariantCreationRequest request);

    // Cập nhật Entity từ Update DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "carModel", ignore = true)
    @Mapping(target = "images", ignore = true)
    void updateCarVariant(@MappingTarget CarVariant carVariant, CarVariantUpdateRequest request);

    // Helper method để lấy URL ảnh đầu tiên
    default String getFirstImageUrl(List<CarImage> images) {
        if (images != null && !images.isEmpty()) {
            // Sắp xếp theo ID hoặc trường display_order nếu có, rồi lấy ảnh đầu
            return images.stream()
                    .findFirst() // Lấy phần tử đầu tiên (có thể thêm sắp xếp nếu cần)
                    .map(CarImage::getImageUrl)
                    .orElse(null);
        }
        return null;
    }
}