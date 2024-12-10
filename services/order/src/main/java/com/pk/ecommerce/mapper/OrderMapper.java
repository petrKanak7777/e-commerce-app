package com.pk.ecommerce.mapper;

import com.pk.ecommerce.kafka.OrderConfirmation;
import com.pk.ecommerce.model.entity.Order;
import com.pk.ecommerce.model.request.OrderRequest;
import com.pk.ecommerce.model.request.PaymentRequest;
import com.pk.ecommerce.model.response.CustomerResponse;
import com.pk.ecommerce.model.response.OrderResponse;
import com.pk.ecommerce.model.response.ProductPurchaseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    @Mapping(target = "totalAmount", source = "amount")
    Order toOrder(OrderRequest request);

    OrderResponse toOrderResponse(Order order);

    @Mapping(target = "amount", source = "request.amount")
    @Mapping(target = "paymentMethod", source = "request.paymentMethod")
    @Mapping(target = "orderReference", source = "request.reference")
    @Mapping(target = "customer.id", source = "customer.id")
    @Mapping(target = "customer.firstName", source = "customer.firstName")
    @Mapping(target = "customer.lastName", source = "customer.lastName")
    @Mapping(target = "customer.email", source = "customer.email")
    PaymentRequest toPaymentRequest(OrderRequest request, Integer orderId, CustomerResponse customer);

    @Mapping(target = "orderReference", source = "request.reference")
    @Mapping(target = "totalAmount", source = "request.amount")
    @Mapping(target = "paymentMethod", source = "request.paymentMethod")
    @Mapping(target = "customerResponse", source = "customer")
    OrderConfirmation toOrderConfirmation(OrderRequest request, CustomerResponse customer, List<ProductPurchaseResponse> products);
}