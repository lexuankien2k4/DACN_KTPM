package com.Nhom7.DACN_KTPM.repository;

import com.Nhom7.DACN_KTPM.entity.CarCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarCategoryRepository extends JpaRepository<CarCategory, Integer> {

    // Kiểm tra tên tồn tại (dùng khi tạo/cập nhật)
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Integer id); // Kiểm tra tên tồn tại (ngoại trừ chính nó)
}
