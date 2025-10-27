package com.Nhom7.DACN_KTPM.repository;


import com.Nhom7.DACN_KTPM.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {}
