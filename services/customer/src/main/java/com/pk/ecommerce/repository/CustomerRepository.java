package com.pk.ecommerce.repository;

import com.pk.ecommerce.model.entity.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
}