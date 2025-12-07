package com.golfRental.domain.image.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.image.dto.request.PresignedUrlRequest;
import com.golfRental.domain.image.dto.response.PresignedUrlResponse;
import org.springframework.http.ResponseEntity;

public interface ImageController {

    /**
     * Presigned URL 발급 API
     *
     * @param authUser            토큰 정보
     * @param presignedUrlRequest Presigned URL 발급에 필요한 데이터
     * @return PresignedUrlResponse
     */
    ResponseEntity<CommonApiResponse<PresignedUrlResponse>> getPresignedUrl(
            AuthUser authUser,
            PresignedUrlRequest presignedUrlRequest
    );
}
