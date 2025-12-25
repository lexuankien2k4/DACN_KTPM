package com.Nhom7.DACN_KTPM.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 👇 SỬA ĐOẠN NÀY
        // Ý nghĩa: Khi trình duyệt gọi http://localhost:8080/images/abc.png
        // Hệ thống sẽ tìm file abc.png trong thư mục: src/main/resources/public/images/
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/public/images/");
    }
//trước khi thay đổi để chạy ngrok
    /*@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }*/
    //chạy ngrok
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // 👈 Thay .allowedOrigins bằng .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}