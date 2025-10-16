package com.Nhom7.DACN_KTPM.mapper;


import com.Nhom7.DACN_KTPM.dto.request.RoleRequest;
import com.Nhom7.DACN_KTPM.dto.response.RoleResponse;
import com.Nhom7.DACN_KTPM.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
