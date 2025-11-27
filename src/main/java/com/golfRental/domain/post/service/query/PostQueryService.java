package com.golfRental.domain.post.service.query;

import com.golfRental.domain.post.entity.Post;

public interface PostQueryService {

    Post findById(Long postId);
}
