package com.pk.ecommerce.kafka.consumer;

import com.pk.ecommerce.kafka.OrderConfirmation;
import com.pk.ecommerce.kafka.PaymentConfirmation;
import com.pk.ecommerce.service.EmailService;
import com.pk.ecommerce.service.NotificationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {
    private final NotificationService notificationService;
    private final EmailService emailService;

    @KafkaListener(topics = "${application.config.kafka.payment-topic}")
    public void consumePaymentSuccessNotification(PaymentConfirmation paymentConfirmation) throws MessagingException {
        log.info("Consuming the message from: ${application.config.kafka.payment-topic}, paymentConfirmation=[{}]",
                paymentConfirmation);

        notificationService.savePaymentConfirmation(paymentConfirmation);

        var customerName = paymentConfirmation.customerFirstName() + " " + paymentConfirmation.customerLastName();
        emailService.sendPaymentSuccessEmail(
                paymentConfirmation.customerEmail(),
                customerName,
                paymentConfirmation.amount(),
                paymentConfirmation.orderReference()
        );
    }

    @KafkaListener(topics = "${application.config.kafka.order-topic}")
    public void consumeOrderSuccessConfirmation(OrderConfirmation orderConfirmation) throws MessagingException {
        log.info("Consuming the message from: ${application.config.kafka.order-topic}, orderConfirmation=[{}]",
                orderConfirmation);

        notificationService.saveOrderConfirmation(orderConfirmation);

        var customerName = orderConfirmation.customerResponse().firstName()
                + " " + orderConfirmation.customerResponse().lastName();
        emailService.sendPaymentSuccessEmail(
                orderConfirmation.customerResponse().email(),
                customerName,
                orderConfirmation.totalAmount(),
                orderConfirmation.orderReference()
        );
    }
}