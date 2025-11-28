package com.golfRental.domain.post.repository;

import com.golfRental.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
                    SELECT p FROM Post p
                    ORDER BY
                        CASE p.tradeStatus
                            WHEN 'BEFORE_TRANSACTION' THEN 0
                            WHEN 'TRADING' THEN 1
                            WHEN 'TRANSACTION_COMPLETED' THEN 2
                        END ASC,
                        p.createdAt DESC
            """)
    Slice<Post> findAllOrderByStatus(Pageable pageable);
}
