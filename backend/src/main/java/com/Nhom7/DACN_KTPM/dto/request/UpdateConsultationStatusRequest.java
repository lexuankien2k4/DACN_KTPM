package com.Nhom7.DACN_KTPM.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UpdateConsultationStatusRequest {


    private String newStatus;

    private LocalDateTime scheduledAt;
}