package com.Nhom7.DACN_KTPM.service;

import com.Nhom7.DACN_KTPM.dto.response.ProductDisplayResponse;
import com.Nhom7.DACN_KTPM.entity.CarModel;
import com.Nhom7.DACN_KTPM.entity.CarVariant;
import com.Nhom7.DACN_KTPM.repository.CarModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicProductService {

    private final CarModelRepository carModelRepository;

    public List<ProductDisplayResponse> getProductsByCategory(String categoryType) {
        Integer categoryId;

        // Xác định ID danh mục (Vẫn giữ Integer vì bảng Category dùng int)
        switch (categoryType.toLowerCase()) {
            case "ev": categoryId = 1; break;
            case "gasoline": categoryId = 2; break;
            case "service": categoryId = 3; break;
            default: categoryId = 1;
        }

        // Tìm model theo categoryId (Hàm này nhận Integer - ĐÚNG)
        List<CarModel> models = carModelRepository.findByCategoryId(categoryId);

        List<ProductDisplayResponse> responses = new ArrayList<>();

        for (CarModel model : models) {
            // Lấy biến thể đầu tiên làm đại diện
            CarVariant representativeVariant = model.getVariants().stream().findFirst().orElse(null);

            if (representativeVariant != null) {
                // Lấy ảnh đầu tiên
                String imageUrl = representativeVariant.getImages().stream()
                        .findFirst()
                        .map(img -> "http://localhost:8080/" + img.getImageUrl())
                        .orElse("/assets/image/default.png");

                responses.add(ProductDisplayResponse.builder()
                        .id(model.getId()) // model.getId() là Long -> Khớp
                        .name(model.getName())
                        .price(representativeVariant.getPrice())
                        .image(imageUrl)
                        .range((representativeVariant.getRangeNedc() != null ? representativeVariant.getRangeNedc() : 0) + " km")
                        .type(model.getBodyType())
                        .categoryName(model.getCategory().getName())
                        .seats("5")
                        .build());
            }
        }
        return responses;
    }
}