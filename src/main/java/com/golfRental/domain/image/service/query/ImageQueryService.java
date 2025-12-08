package com.golfRental.domain.image.service.query;

import com.golfRental.domain.image.entity.Image;

import java.util.List;

public interface ImageQueryService {

    List<Image> findAllByImage(List<Long> imageIds);
}
