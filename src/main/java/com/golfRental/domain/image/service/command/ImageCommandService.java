package com.golfRental.domain.image.service.command;

import com.golfRental.domain.image.dto.request.PresignedUrlRequest;
import com.golfRental.domain.image.dto.response.PresignedUrlResponse;

public interface ImageCommandService {

    /**
     * Presigned URL 발급
     *
     * @param userId              인증된 유저 ID
     * @param presignedUrlRequest Presigned URL 발급에 필요한 데이터
     * @return PresignedUrlResponse
     */
    PresignedUrlResponse getPresignedUrl(Long userId, PresignedUrlRequest presignedUrlRequest);
}
