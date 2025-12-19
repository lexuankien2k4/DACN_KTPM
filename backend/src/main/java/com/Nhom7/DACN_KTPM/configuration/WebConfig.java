package com.Nhom7.DACN_KTPM.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 1. Cấu hình CORS: Cho phép Frontend  gọi API
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    // 2. Cấu hình hiển thị ảnh: Map URL /images/** vào thư mục uploads trên máy
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Lấy đường dẫn tuyệt đối tới thư mục "uploads" trong project
        String uploadPath = Paths.get("uploads").toAbsolutePath().toUri().toString();

        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }
}