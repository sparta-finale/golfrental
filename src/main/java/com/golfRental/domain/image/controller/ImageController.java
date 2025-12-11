package com.golfRental.domain.image.controller;

import com.golfRental.common.response.CommonApiResponse;
import com.golfRental.domain.auth.dto.AuthUser;
import com.golfRental.domain.image.dto.request.ImageSaveRequest;
import com.golfRental.domain.image.dto.request.PresignedUrlRequest;
import com.golfRental.domain.image.dto.response.ImageSavedResponse;
import com.golfRental.domain.image.dto.response.PresignedUrlResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "이미지 관리", description = "이미지 관련 API")
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

    /**
     * 이미지 메타데이터 저장 API
     *
     * @param authUser         토큰 정보
     * @param imageSaveRequest 저장할 이미지 데이터
     * @return ImageSaveResponse
     */
    @Operation(
            summary = "이미지 메타데이터 저장",
            description = "S3에 이미지 업로드를 완료한 후, 해당 이미지의 메타데이터를 서버에 저장합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "201", description = "생성 성공"),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "401", description = "인증 실패"),
                    @ApiResponse(responseCode = "404", description = "요청 리소스를 찾을 수 없음")
            }
    )
    ResponseEntity<CommonApiResponse<ImageSavedResponse>> saveImage(
            AuthUser authUser,
            ImageSaveRequest imageSaveRequest
    );
}
