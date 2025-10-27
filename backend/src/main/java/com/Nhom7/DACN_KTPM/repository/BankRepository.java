package com.Nhom7.DACN_KTPM.repository;

import com.Nhom7.DACN_KTPM.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankRepository extends JpaRepository<Bank, Integer> { // ID is Integer

    // Find active banks, ordered by name
    List<Bank> findByIsActiveTrueOrderByNameAsc();

    // Check if name exists (for create/update validation)
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Integer id);
}