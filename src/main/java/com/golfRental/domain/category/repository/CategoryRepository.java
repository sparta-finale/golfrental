package com.golfRental.domain.category.repository;

import com.golfRental.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("COUNT(c) > 0 " +
            "FROM Category c " +
            "WHERE c.name = :name " +
            "AND c.deletedAt IS NULL")
    boolean existsByNameAndNotDeleted(@Param("name") String name);
}
