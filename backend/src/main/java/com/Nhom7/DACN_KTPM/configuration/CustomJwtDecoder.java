package com.Nhom7.DACN_KTPM.configuration;


import com.Nhom7.DACN_KTPM.dto.request.IntrospectRequest;
import com.Nhom7.DACN_KTPM.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.signerKey}")
    private String signerKey;

    private NimbusJwtDecoder nimbusJwtDecoder;

    @Override
    public Jwt decode(String token) throws JwtException {

        if (nimbusJwtDecoder == null) {
            throw new JwtException("Lỗi cấu hình nội bộ JwtDecoder");
        }
        try {
            return nimbusJwtDecoder.decode(token);
        } catch (Exception e) {
            throw new JwtException(e.getMessage());
        }
    }

    @PostConstruct
    public void init() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
        nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
}