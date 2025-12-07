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

    @Column(nullable = false)
    private boolean is_thumbnail;

    @Column(nullable = false)
    private int sort_order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @Builder
    private PostImage(boolean is_thumbnail, int sort_order, Post post, Image image) {
        this.is_thumbnail = is_thumbnail;
        this.sort_order = sort_order;
        this.post = post;
        this.image = image;
    }
}
