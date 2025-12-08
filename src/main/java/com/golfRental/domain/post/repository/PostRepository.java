package com.golfRental.domain.post.repository;

import com.golfRental.domain.category.entity.Category;
import com.golfRental.domain.post.entity.Post;
import com.golfRental.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
                    SELECT DISTINCT p FROM Post p
                                JOIN FETCH p.user
                                JOIN FETCH p.category
                                LEFT JOIN FETCH p.postImages pi
                                LEFT JOIN FETCH pi.image
                    WHERE p.deletedAt IS NULL
                    ORDER BY
                        CASE p.tradeStatus
                            WHEN 'BEFORE_TRANSACTION' THEN 0
                            WHEN 'TRADING' THEN 1
                            WHEN 'TRANSACTION_COMPLETED' THEN 2
                        END ASC
            """)
    Slice<Post> findAllOrderByStatus(Pageable pageable);

    @Query("""
            SELECT DISTINCT p FROM Post p
                    JOIN FETCH p.user
                    JOIN FETCH p.category
                    LEFT JOIN FETCH p.postImages pi
                    LEFT JOIN FETCH pi.image
            WHERE p.id = :postId AND p.deletedAt IS NULL
            """)
    Optional<Post> findByIdWithUserAndCategory(@Param("postId") Long postId);

    @Query("""
                    SELECT DISTINCT p FROM Post p
                                JOIN FETCH p.user
                                JOIN FETCH p.category
                                LEFT JOIN FETCH p.postImages pi
                                LEFT JOIN FETCH pi.image
                    WHERE p.user = :user AND p.deletedAt IS NULL
                    ORDER BY
                        CASE p.tradeStatus
                            WHEN 'BEFORE_TRANSACTION' THEN 0
                            WHEN 'TRADING' THEN 1
                            WHEN 'TRANSACTION_COMPLETED' THEN 2
                        END ASC
            """)
    Slice<Post> findAllByUserOrderByStatus(@Param("user") User user, Pageable pageable);

    @Query("""
                    SELECT p FROM Post p
                                JOIN FETCH p.user
                                JOIN FETCH p.category
                    WHERE p.category = :category AND p.deletedAt IS NULL
                    ORDER BY
                        CASE p.tradeStatus
                            WHEN 'BEFORE_TRANSACTION' THEN 0
                            WHEN 'TRADING' THEN 1
                            WHEN 'TRANSACTION_COMPLETED' THEN 2
                        END ASC
            """)
    Slice<Post> findAllByCategoryOrderByStatus(@Param("category") Category category, Pageable pageable);

    Optional<Post> findByIdAndDeletedAtIsNull(Long id);
}
