package com.golfRental.domain.image.service.query;

import com.golfRental.domain.image.entity.Image;
import com.golfRental.domain.image.exception.ImageErrorCode;
import com.golfRental.domain.image.exception.ImageException;
import com.golfRental.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageQueryServiceImpl implements ImageQueryService {

    private final ImageRepository imageRepository;

    @Override
    public List<Image> findAllByImage(List<Long> imageIds) {
        List<Image> images = imageRepository.findAllByIdInAndDeletedAtIsNull(imageIds);

        if (images.size() != imageIds.size()) {
            throw new ImageException(ImageErrorCode.IMAGE_NOT_FOUND);
        }

        return images;
    }
}
