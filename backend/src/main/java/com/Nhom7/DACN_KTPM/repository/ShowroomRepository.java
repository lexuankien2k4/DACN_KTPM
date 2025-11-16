package com.Nhom7.DACN_KTPM.repository;

import com.Nhom7.DACN_KTPM.entity.Showroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShowroomRepository extends JpaRepository<Showroom, Integer> {
    List<Showroom> findByProvince(String province);

    @Query("SELECT DISTINCT s.province FROM Showroom s ORDER BY s.province")
    List<String> findDistinctProvinces();
}