package com.Nhom7.DACN_KTPM.mapper;


import com.Nhom7.DACN_KTPM.dto.request.PermissionRequest;
import com.Nhom7.DACN_KTPM.dto.response.PermissionResponse;
import com.Nhom7.DACN_KTPM.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
