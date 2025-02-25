package com.base.auth.repository;

import com.base.auth.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {
    News findNewsByTitle(String title);

    @Query("SELECT COUNT(n) FROM News n " +
            "WHERE n.category.id = :categoryId")
    Long countNewsByCategoryId(@Param("categoryId") Long categoryId);

}