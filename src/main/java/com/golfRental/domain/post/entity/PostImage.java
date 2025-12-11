package com.golfRental.domain.post.entity;

import com.golfRental.common.entity.BaseEntity;
import com.golfRental.domain.image.entity.Image;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "post_images", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"post_id", "image_id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage extends BaseEntity {

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

    private PostImage(Boolean isThumbnail, Integer sortOrder, Post post, Image image) {
        this.isThumbnail = isThumbnail;
        this.sortOrder = sortOrder;
        this.post = post;
        this.image = image;
    }

    public static PostImage create(Boolean isThumbnail, Integer sortOrder, Post post, Image image) {
        return new PostImage(isThumbnail, sortOrder, post, image);
    }

    public void updateThumbnail(boolean isThumbnail) {
        this.isThumbnail = isThumbnail;
    }
}
