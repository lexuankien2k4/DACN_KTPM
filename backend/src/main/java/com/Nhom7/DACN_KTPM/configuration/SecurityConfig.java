package com.Nhom7.DACN_KTPM.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Bật @PreAuthorize
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig {

    // Tiêm 2 bean custom của bạn vào
    CustomJwtDecoder customJwtDecoder;
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    // Đường dẫn công khai, không cần đăng nhập
    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/models",
            "/api/models/active",
            "/api/variants",
            "/api/variants/active-variants",
            "/api/variants/{id}/details",
            "/api/banks",
            "/api/banks/active",
            "/api/finance/policies/active",
            "/api/finance/calculate",
            "/auth/token",
            "/api/finance/policies/by-bank/**",

    };
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/images/**",  // Bỏ qua tất cả ảnh tĩnh
                "/css/**",     // Bỏ qua CSS (nếu có)
                "/js/**"       // Bỏ qua JS (nếu có)
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Tắt CSRF cho REST API

                // 1. Phân quyền truy cập
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll() // Cho phép truy cập các API công khai
                        .anyRequest().authenticated() // Tất cả các API còn lại phải xác thực
                )

                // 2. Cấu hình xác thực (thay cho formLogin)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(customJwtDecoder)) // Dùng CustomJwtDecoder của bạn
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) // Dùng EntryPoint để xử lý lỗi 401
                )

                // 3. Đặt chế độ STATELESS (quan trọng nhất)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .build();
    }

    // (Bean PasswordEncoder của bạn, giữ nguyên)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}