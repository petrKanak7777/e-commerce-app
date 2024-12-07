package com.pk.ecommerce.service;

import com.pk.ecommerce.kafka.producer.NotificationProducer;
import com.pk.ecommerce.mapper.PaymentMapper;
import com.pk.ecommerce.model.request.PaymentRequest;
import com.pk.ecommerce.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final NotificationProducer notificationProducer;

    public Integer createPayment(PaymentRequest request) {
        var payment = paymentRepository.save(paymentMapper.toPayment(request));

        notificationProducer.sendNotification(paymentMapper.toPaymentNotificationRequest(request));

        return payment.getId();
    }
}