package com.Nhom7.DACN_KTPM.repository;

import com.Nhom7.DACN_KTPM.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    boolean existsByDepositId(Long depositId);
    boolean existsByContractNumber(String contractNumber);
}