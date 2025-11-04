package com.Nhom7.DACN_KTPM.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Cho phép tất cả các API (`/api/**`)
                registry.addMapping("/**")
                        // Cho phép frontend ở cổng 3000 truy cập
                        .allowedOrigins("http://localhost:3000")
                        // Cho phép các phương thức này
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        // Cho phép tất cả các header
                        .allowedHeaders("*")
                        // Cho phép gửi cookie (nếu cần cho đăng nhập)
                        .allowCredentials(true);
            }
        };
    }
}