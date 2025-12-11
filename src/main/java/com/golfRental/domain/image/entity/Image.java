package com.golfRental.domain.image.entity;

import com.golfRental.common.entity.BaseEntity;
import com.golfRental.domain.image.enums.ImageType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String fileName;

    @Column(length = 2048, nullable = false)
    private String url;

    @Column(length = 300, nullable = false)
    private String s3Key;

    @Column(length = 100, nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long size;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ImageType type;

    private Image(
            String url,
            String fileName,
            String s3Key,
            String contentType,
            Long size,
            ImageType type
    ) {
        this.url = url;
        this.fileName = fileName;
        this.s3Key = s3Key;
        this.contentType = contentType;
        this.size = size;
        this.type = type;
    }

    public static Image create(
            String url, String fileName, String s3Key,
            String contentType, Long size, ImageType type
    ) {
        return new Image(url, fileName, s3Key, contentType, size, type);
    }
}
