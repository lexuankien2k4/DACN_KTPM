package com.Nhom7.DACN_KTPM.mapper;


import com.Nhom7.DACN_KTPM.dto.request.UserCreationRequest;
import com.Nhom7.DACN_KTPM.dto.request.UserUpdateRequest;
import com.Nhom7.DACN_KTPM.dto.response.UserResponse;
import com.Nhom7.DACN_KTPM.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
