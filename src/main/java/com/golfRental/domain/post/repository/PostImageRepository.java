package com.golfRental.domain.post.repository;

import com.golfRental.domain.image.entity.Image;
import com.golfRental.domain.post.entity.Post;
import com.golfRental.domain.post.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    boolean existsByPostAndImageAndDeletedAtIsNull(Post post, Image image);
}
