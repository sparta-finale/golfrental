package com.golfRental.domain.image.repository;

import com.golfRental.domain.image.entity.Image;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, Long> {
}
