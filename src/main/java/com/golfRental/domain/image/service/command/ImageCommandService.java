package com.golfRental.domain.image.service.command;

import com.golfRental.domain.image.dto.request.ImageSaveRequest;
import com.golfRental.domain.image.dto.request.PresignedUrlRequest;
import com.golfRental.domain.image.dto.response.ImageSavedResponse;
import com.golfRental.domain.image.dto.response.PresignedUrlResponse;

public interface ImageCommandService {

    /**
     * Presigned URL 발급
     *
     * @param presignedUrlRequest Presigned URL 발급에 필요한 데이터
     * @return PresignedUrlResponse
     */
    PresignedUrlResponse getPresignedUrl(PresignedUrlRequest presignedUrlRequest);

    /**
     * 이미지 메타데이터 저장
     *
     * @param imageSaveRequest 저장할 메타데이터
     * @return ImageSavedResponse
     */
    ImageSavedResponse saveImage(ImageSaveRequest imageSaveRequest);
}
