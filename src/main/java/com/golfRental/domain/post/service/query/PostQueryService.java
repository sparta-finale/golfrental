package com.golfRental.domain.post.service.query;

import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.post.dto.response.PostGetAllResponse;
import com.golfRental.domain.post.dto.response.PostGetsResponse;
import com.golfRental.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;

public interface PostQueryService {

    // 게시물 목록 조회
    SliceResponse<PostGetAllResponse> getAll(Pageable pageable);

    // 게시물 상세 조회
    PostGetsResponse getPost(Long postId);

    Post findById(Long postId);
}
