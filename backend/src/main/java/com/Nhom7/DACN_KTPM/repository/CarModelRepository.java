package com.Nhom7.DACN_KTPM.repository;

import com.Nhom7.DACN_KTPM.entity.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
// 👇 QUAN TRỌNG: Phải là Long để khớp với Service và Entity
public interface CarModelRepository extends JpaRepository<CarModel, Long> {

    boolean existsByName(String name);

    Optional<CarModel> findByName(String name);

    List<CarModel> findByCategoryId(Integer categoryId);

    List<CarModel> findByIsActiveTrueOrderByNameAsc();
}