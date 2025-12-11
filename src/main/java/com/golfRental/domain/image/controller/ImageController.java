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

    @Operation(
            summary = "Presigned URL 발급",
            description = "Presigned URL 발급합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Presigned URL 발급 성공"),
                    @ApiResponse(responseCode = "400", description = "유효하지 않은 파일명 실패"),
                    @ApiResponse(responseCode = "400", description = "유효하지 않은 Content-Type 실패"),
                    @ApiResponse(responseCode = "400", description = "유효하지 않은 Primary-Type 실패"),
                    @ApiResponse(responseCode = "400", description = "유효하지 않은 Sub-Type 실패"),
                    @ApiResponse(responseCode = "400", description = "확장자와 Sub-Type 불일치 실패"),
                    @ApiResponse(responseCode = "400", description = "지원하지 않는 파일 형식 실패"),
                    @ApiResponse(responseCode = "500", description = " Presigned URL 생성에 실패")
            }
    )
    ResponseEntity<CommonApiResponse<PresignedUrlResponse>> getPresignedUrl(
            AuthUser authUser,
            PresignedUrlRequest presignedUrlRequest
    );

    @Operation(
            summary = "이미지 메타데이터 저장",
            description = "이미지의 메타데이터를 저장합니다.",
            security = {@SecurityRequirement(name = "bearerAuth")},
            responses = {
                    @ApiResponse(responseCode = "201", description = "이미지 메타데이터 저장 성공"),
                    @ApiResponse(responseCode = "400", description = "유효하지 않은 파일명 실패"),
                    @ApiResponse(responseCode = "400", description = "지원하지 않는 파일 형식 실패"),
                    @ApiResponse(responseCode = "400", description = "유효하지 않은 Content-Type 실패"),
                    @ApiResponse(responseCode = "400", description = "유효하지 않은 Primary-Type 실패"),
                    @ApiResponse(responseCode = "400", description = "유효하지 않은 Sub-Type 실패"),
                    @ApiResponse(responseCode = "400", description = "확장자와 Sub-Type 불일치 실패")
            }
    )
    ResponseEntity<CommonApiResponse<ImageSavedResponse>> saveImage(
            AuthUser authUser,
            ImageSaveRequest imageSaveRequest
    );
}
