package com.golfRental.domain.post.service.command;

import com.golfRental.domain.post.entity.Post;

public interface PostCommandService {

    Post findById(Long postId);
}
