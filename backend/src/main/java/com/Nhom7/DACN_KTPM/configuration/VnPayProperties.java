package com.Nhom7.DACN_KTPM.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "vnp")
@Getter
@Setter
public class VnPayProperties {
    private String payUrl;
    private String tmnCode;
    private String hashSecret;
    private String apiUrl;
    private String returnUrl;
}