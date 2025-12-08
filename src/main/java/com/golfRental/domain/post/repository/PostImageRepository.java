package com.golfRental.domain.post.repository;

import com.golfRental.domain.post.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    @Query("""
            SELECT pi FROM PostImage pi
                JOIN FETCH pi.post p
                JOIN FETCH pi.image i
            WHERE p.deletedAt IS NULL
                AND i.deletedAt IS NULL
            ORDER BY pi.sortOrder ASC
            """)
    List<PostImage> findAllByIdWithDetail();
}
