package com.pk.ecommerce.mapper;

import com.pk.ecommerce.model.entity.Order;
import com.pk.ecommerce.model.request.OrderRequest;
import com.pk.ecommerce.model.response.OrderResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class OrderMapper {

    private final OrderLineMapper orderLineMapper;

    public Order toOrder(OrderRequest request) {
        if (Objects.isNull(request)) {
            throw new NullPointerException("Input order request value is null");
        }

        return Order.builder()
                .id(request.id())
                .reference(request.reference())
                .totalAmount(request.amount())
                .paymentMethod(request.paymentMethod())
                .customerId(request.customerId())
                .build();
    }

    public OrderResponse toOrderResponse(Order order) {
        if (Objects.isNull(order)) {
            throw new NullPointerException("Input order value is null");
        }

        return new OrderResponse(
                order.getId(),
                order.getReference(),
                order.getTotalAmount(),
                order.getPaymentMethod(),
                order.getCustomerId()
        );
    }
}