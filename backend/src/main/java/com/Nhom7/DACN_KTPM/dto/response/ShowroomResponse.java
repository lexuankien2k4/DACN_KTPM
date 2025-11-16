package com.Nhom7.DACN_KTPM.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShowroomResponse {
    Integer id;
    String name;
    String address;
    String province;
    String district;
}
