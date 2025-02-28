package com.base.auth.repository;

import com.base.auth.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    public Category findCategoryByName(String name);
    boolean existsByName(String name);
}
