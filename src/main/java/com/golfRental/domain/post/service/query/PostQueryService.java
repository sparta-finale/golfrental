package com.golfRental.domain.post.service.query;

import com.golfRental.common.response.SliceResponse;
import com.golfRental.domain.post.dto.response.PostGetAllResponse;
import com.golfRental.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;

public interface PostQueryService {

    SliceResponse<PostGetAllResponse> getAll(Pageable pageable);

    Post findById(Long postId);
}
