package com.pk.ecommerce.service;

import com.pk.ecommerce.kafka.OrderConfirmation;
import com.pk.ecommerce.kafka.PaymentConfirmation;
import com.pk.ecommerce.mapper.NotificationMapper;
import com.pk.ecommerce.repository.NotoficationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.pk.ecommerce.model.enums.NotificationType.ORDER_CONFIRMATION;
import static com.pk.ecommerce.model.enums.NotificationType.PAYMENT_CONFIRMATION;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotoficationRepository notoficationRepository;
    private final NotificationMapper notificationMapper;

    public void savePaymentConfirmation(PaymentConfirmation paymentConfirmation) {
        notoficationRepository.save(
                notificationMapper.addPaymentConfirmationToNotification(
                        paymentConfirmation, LocalDateTime.now(), PAYMENT_CONFIRMATION));
    }

    public void saveOrderConfirmation(OrderConfirmation orderConfirmation) {
        notoficationRepository.save(
                notificationMapper.addOrderConfirmationToNotification(
                        orderConfirmation, LocalDateTime.now(), ORDER_CONFIRMATION));
    }
}