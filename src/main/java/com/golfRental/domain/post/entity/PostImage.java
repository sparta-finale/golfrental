package com.golfRental.domain.post.entity;

import com.golfRental.domain.image.entity.Image;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "post_images", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"post_id", "image_id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_thumbnail", nullable = false)
    private Boolean isThumbnail;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @Builder
    private PostImage(Boolean isThumbnail, Integer sortOrder, Post post, Image image) {
        this.isThumbnail = isThumbnail;
        this.sortOrder = sortOrder;
        this.post = post;
        this.image = image;
    }
}
