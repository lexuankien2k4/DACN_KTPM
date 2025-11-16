package com.Nhom7.DACN_KTPM.mapper;

import com.Nhom7.DACN_KTPM.dto.response.ShowroomResponse;
import com.Nhom7.DACN_KTPM.entity.Showroom;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ShowroomMapper {
    ShowroomResponse toShowroomResponse(Showroom showroom);

    List<ShowroomResponse> toShowroomResponseList(List<Showroom> showrooms);
}
