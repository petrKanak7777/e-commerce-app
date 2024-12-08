package com.pk.ecommerce.repository;

import com.pk.ecommerce.model.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotoficationRepository extends MongoRepository<Notification, String> {
}