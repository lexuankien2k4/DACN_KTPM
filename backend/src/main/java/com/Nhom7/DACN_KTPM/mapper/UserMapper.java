package com.Nhom7.DACN_KTPM.mapper;

import com.Nhom7.DACN_KTPM.dto.request.UserCreationRequest;
import com.Nhom7.DACN_KTPM.dto.request.UserUpdateRequest;
import com.Nhom7.DACN_KTPM.dto.response.UserResponse;
import com.Nhom7.DACN_KTPM.entity.Role;
import com.Nhom7.DACN_KTPM.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy; // Nhớ Import dòng này

import java.util.Set;
import java.util.stream.Collectors;

// 👇 QUAN TRỌNG: Thêm unmappedTargetPolicy = ReportingPolicy.IGNORE
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapListRolesToSet")
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    @Named("mapListRolesToSet")
    default Set<Role> mapListRolesToSet(java.util.List<String> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(roleName -> Role.builder().name(roleName).build())
                .collect(Collectors.toSet());
    }
}