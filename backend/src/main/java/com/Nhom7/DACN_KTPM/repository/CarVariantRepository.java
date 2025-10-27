package com.Nhom7.DACN_KTPM.repository;

import com.Nhom7.DACN_KTPM.entity.CarVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarVariantRepository extends JpaRepository<CarVariant, Integer> {

    // Tìm các phiên bản theo model_id
    List<CarVariant> findByCarModelIdAndIsActiveTrueOrderByNameAsc(Integer modelId);
    List<CarVariant> findByCarModelIdOrderByNameAsc(Integer modelId); // Lấy cả inactive cho admin

    // Kiểm tra tên tồn tại trong cùng một model (dùng khi tạo/cập nhật)
    boolean existsByNameAndCarModelId(String name, Integer modelId);
    boolean existsByNameAndCarModelIdAndIdNot(String name, Integer modelId, Integer id);
}