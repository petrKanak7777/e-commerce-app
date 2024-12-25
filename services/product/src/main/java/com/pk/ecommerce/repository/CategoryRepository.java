package com.pk.ecommerce.repository;

import com.pk.ecommerce.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
