package com.pk.ecommerce.mapper;

import com.pk.ecommerce.kafka.PaymentNotificationRequest;
import com.pk.ecommerce.model.entity.Payment;
import com.pk.ecommerce.model.request.PaymentRequest;
import com.pk.ecommerce.model.response.PaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {
    Payment toPayment(PaymentRequest request);
    PaymentResponse toPaymentResponse(Payment payment, String customerId);

    @Mapping(target = "customerFirstName", source = "request.customer.firstName")
    @Mapping(target = "customerLastName", source = "request.customer.lastName")
    @Mapping(target = "customerEmail", source = "request.customer.email")
    PaymentNotificationRequest toPaymentNotificationRequest(PaymentRequest request);
}
