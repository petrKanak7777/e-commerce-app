package com.pk.ecommerce.mapper;

import com.pk.ecommerce.kafka.OrderConfirmation;
import com.pk.ecommerce.kafka.PaymentConfirmation;
import com.pk.ecommerce.model.enums.NotificationType;
import com.pk.ecommerce.model.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.time.LocalDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationMapper {

    @Mapping(target = "paymentConfirmation", source = "paymentConfirmation")
    Notification addPaymentConfirmationToNotification(
            PaymentConfirmation paymentConfirmation,
            LocalDateTime notificationDate,
            NotificationType type);

    @Mapping(target = "orderConfirmation", source = "orderConfirmation")
    Notification addOrderConfirmationToNotification(
            OrderConfirmation orderConfirmation,
            LocalDateTime notificationDate,
            NotificationType type);
}