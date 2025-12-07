package com.golfRental.domain.image.repository;

import com.golfRental.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByIdInAndDeletedAtIsNull(Collection<Long> ids);
}
