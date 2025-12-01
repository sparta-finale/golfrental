package com.golfRental.domain.post.repository;

import com.golfRental.domain.post.entity.Post;
import com.golfRental.domain.post.entity.PostFavorites;
import com.golfRental.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostFavoritesRepository extends JpaRepository<PostFavorites, Long> {
    boolean existsByUserAndPost(User user, Post post);

    List<PostFavorites> findByUser(User user);
}
