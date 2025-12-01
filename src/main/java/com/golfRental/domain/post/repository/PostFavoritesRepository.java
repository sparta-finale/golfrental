package com.golfRental.domain.post.repository;

import com.golfRental.domain.post.entity.Post;
import com.golfRental.domain.post.entity.PostFavorites;
import com.golfRental.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface PostFavoritesRepository extends JpaRepository<PostFavorites, Long> {
    boolean existsByUserAndPost(User user, Post post);

    @Query("SELECT pf.post.id FROM PostFavorites pf WHERE pf.user = :user")
    Set<Long> findPostIdsByUser(@Param("user") User user);
}
