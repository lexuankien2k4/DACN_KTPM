package com.Nhom7.DACN_KTPM.repository;

import com.Nhom7.DACN_KTPM.entity.LoanPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanPolicyRepository extends JpaRepository<LoanPolicy, Integer> {

    List<LoanPolicy> findByIsActiveTrue();
    List<LoanPolicy> findByBankIdAndIsActiveTrue(Integer bankId);

    // Check if a policy with the same name exists for the same bank
    boolean existsByNameAndBankId(String name, Integer bankId);
    boolean existsByNameAndBankIdAndIdNot(String name, Integer bankId, Integer id);
}
