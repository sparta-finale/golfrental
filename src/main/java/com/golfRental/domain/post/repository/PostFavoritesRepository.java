package com.golfRental.domain.post.repository;

import com.golfRental.domain.post.entity.PostFavorites;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFavoritesRepository extends JpaRepository<PostFavorites, Long> {
}
