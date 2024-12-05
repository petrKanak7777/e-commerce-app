package com.pk.ecommerce.repository;

import com.pk.ecommerce.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {


    List<Product> findAllByIdInOrderById(List<Integer> productsId);
}