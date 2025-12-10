package com.golfRental.common.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Golf Rental API")
                        .description("""
                                Golf Rental 플랫폼의 공식 API 문서입니다.

                                이 플랫폼은 **골프 용품 대여 서비스**를 핵심으로 제공하며, 사용자 간 **안전한 골프 용품 대여 거래**를 지원합니다.

                                **주요 특징:**
                                1. **회원 기반 서비스:** 골프 용품 등록, 예약, 즐겨찾기는 회원 전용 기능입니다.
                                2. **비회원 접근:** 공개 게시물 조회 및 카테고리별 검색은 비회원도 가능합니다.
                                3. **직거래 기반:** 모든 대여 거래는 사용자 간의 직거래를 기본으로 하며, 수령/반납 방법을 선택할 수 있습니다.
                                4. **거래 상태 관리:** 거래 전, 거래 중, 거래 완료 상태를 통해 체계적인 대여 관리가 가능합니다.
                                5. **이미지 기반 게시물:** 모든 골프 용품은 썸네일을 포함한 이미지와 함께 등록되어 신뢰성을 높입니다.

                                API는 인증, 게시물, 예약, 카테고리, 이미지 등 모든 기능을 RESTful하게 제공합니다.
                                """)
                        .version("v1.0.0"));
    }
}
