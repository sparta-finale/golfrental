package com.golfRental.domain.image.service.command;

import com.golfRental.domain.image.dto.request.ImageSaveRequest;
import com.golfRental.domain.image.dto.request.PresignedUrlRequest;
import com.golfRental.domain.image.dto.response.ImageSavedResponse;
import com.golfRental.domain.image.dto.response.PresignedUrlResponse;
import com.golfRental.domain.image.entity.Image;
import com.golfRental.domain.image.enums.ImageType;
import com.golfRental.domain.image.exception.ImageErrorCode;
import com.golfRental.domain.image.exception.ImageException;
import com.golfRental.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ImageCommandServiceImpl implements ImageCommandService {

    private static final Duration SIGNED_URL_DURATION = Duration.ofMinutes(5);
    private static final Set<String> ALLOWED_FILE_SUFFIXES = Set.of("jpg", "jpeg", "png", "gif", "webp");
    private static final String PRIMARY_TYPE = "image";

    private final ImageRepository imageRepository;
    private final S3Presigner s3Presigner;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public PresignedUrlResponse getPresignedUrl(PresignedUrlRequest presignedUrlRequest) {
        String extension = getFileExtension(presignedUrlRequest.getFileName());

        String s3Key = createS3Key(presignedUrlRequest.getType(), extension);

        validContentType(presignedUrlRequest.getContentType(), extension);

        String presignedUrl = createPresignedUrl(s3Key, presignedUrlRequest.getContentType());

        String fileUrl = createFileUrl(s3Key);

        return PresignedUrlResponse.builder()
                .presignedUrl(presignedUrl)
                .fileUrl(fileUrl)
                .s3Key(s3Key)
                .contentType(presignedUrlRequest.getContentType())
                .expiresIn((int) SIGNED_URL_DURATION.getSeconds())
                .build();
    }

    @Override
    public ImageSavedResponse saveImage(ImageSaveRequest imageSaveRequest) {
        // 추구 검증 로직 추가적으로 더 늘릴 예정(contentType S3로부터 검증으로 수정, size 검증)
        String extension = getFileExtension(imageSaveRequest.getFileName());
        validContentType(imageSaveRequest.getContentType(), extension);

        Image image = Image.builder()
                .fileName(imageSaveRequest.getFileName())
                .url(imageSaveRequest.getUrl())
                .s3Key(imageSaveRequest.getS3Key())
                .contentType(imageSaveRequest.getContentType())
                .size(imageSaveRequest.getSize())
                .type(imageSaveRequest.getType())
                .build();

        Image savedImage = imageRepository.save(image);

        return ImageSavedResponse.builder()
                .id(savedImage.getId())
                .fileName(savedImage.getFileName())
                .url(savedImage.getUrl())
                .s3Key(savedImage.getS3Key())
                .contentType(savedImage.getContentType())
                .size(savedImage.getSize())
                .type(savedImage.getType().toString().toLowerCase())
                .build();
    }

    // =============================================================== //
    // ======================== HELPER METHOD ======================== //
    private String createS3Key(ImageType imageType, String extension) {
        String s3FileName = UUID.randomUUID() + "." + extension;
        return String.format("%s/%s", imageType.getFolder(), s3FileName);
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");

        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            throw new ImageException(ImageErrorCode.IMAGE_INVALID_FILE_NAME);
        }

        String extension = fileName.substring(lastDotIndex + 1);

        String lowerCaseExtension = extension.toLowerCase();

        if (!ALLOWED_FILE_SUFFIXES.contains(lowerCaseExtension)) {
            throw new ImageException(ImageErrorCode.IMAGE_INVALID_FILE_EXTENSION);
        }

        return extension;
    }

    private String createFileUrl(String s3Key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, s3Key);
    }

    private void validContentType(String contentType, String extension) {
        int slashIndex = contentType.lastIndexOf("/");

        if (slashIndex == -1 || slashIndex == 0 || slashIndex == contentType.length() - 1) {
            throw new ImageException(ImageErrorCode.IMAGE_INVALID_CONTENT_TYPE);
        }

        String lowerPrimaryType = contentType.substring(0, slashIndex).toLowerCase();
        String lowerSubType = contentType.substring(slashIndex + 1).toLowerCase();
        String lowerExtension = extension.toLowerCase();

        if (Objects.equals(lowerExtension, "jpg")) {
            lowerExtension = "jpeg";
        }

        if (!Objects.equals(lowerPrimaryType, PRIMARY_TYPE)) {
            throw new ImageException(ImageErrorCode.IMAGE_INVALID_PRIMARY_TYPE);
        }
        if (!ALLOWED_FILE_SUFFIXES.contains(lowerSubType)) {
            throw new ImageException(ImageErrorCode.IMAGE_INVALID_SUB_TYPE);
        }
        if (!Objects.equals(lowerExtension, lowerSubType)) {
            throw new ImageException(ImageErrorCode.IMAGE_EXTENSION_SUB_TYPE_MISMATCH);
        }
    }

    private String createPresignedUrl(String s3Key, String contentType) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(s3Key)
                    .contentType(contentType)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(SIGNED_URL_DURATION)
                    .putObjectRequest(putObjectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

            return presignedRequest.url().toString();
        } catch (SdkException e) {
            throw new ImageException(ImageErrorCode.IMAGE_PRESIGNED_URL_GENERATION_FAILED);
        }
    }
}
