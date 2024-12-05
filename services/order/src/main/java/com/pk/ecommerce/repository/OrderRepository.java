package com.pk.ecommerce.repository;

import com.pk.ecommerce.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository  extends JpaRepository<Order, Integer> {
}
