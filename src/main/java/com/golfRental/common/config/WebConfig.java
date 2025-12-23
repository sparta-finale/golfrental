package com.golfRental.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * cors.allowed-origins가 없는 환경(local/test)을 대비해
     * default 값을 빈 문자열로 설정
     */
    @Value("${cors.allowed-origins:}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        String[] origins = Arrays.stream(allowedOrigins)
                .filter(StringUtils::hasText)
                .toArray(String[]::new);

        // origin 설정이 없으면 CORS 자체를 등록하지 않음
        if (origins.length == 0) {
            return;
        }

        registry.addMapping("/**")
                .allowedOrigins(origins)
                .allowedMethods(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "HEAD",
                        "OPTIONS"
                )
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
