package com.golfRental.domain.category.repository;

import com.golfRental.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM Category c " +
            "WHERE c.name = :name " +
            "AND c.deletedAt IS NULL")
    boolean existsByNameAndNotDeleted(@Param("name") String name);
}
