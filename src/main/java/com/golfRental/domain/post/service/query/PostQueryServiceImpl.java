package com.golfRental.domain.post.service.query;

import com.golfRental.domain.post.entity.Post;
import com.golfRental.domain.post.exception.PostErrorCode;
import com.golfRental.domain.post.exception.PostException;
import com.golfRental.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;

    @Override
    public Post findById(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new PostException(PostErrorCode.POST_INVALID_ID)
        );
    }
}
