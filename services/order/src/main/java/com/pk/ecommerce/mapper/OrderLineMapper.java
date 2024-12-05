package com.pk.ecommerce.mapper;

import com.pk.ecommerce.model.entity.Order;
import com.pk.ecommerce.model.entity.OrderLine;
import com.pk.ecommerce.model.request.OrderLineRequest;
import com.pk.ecommerce.model.response.OrderLineResponse;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OrderLineMapper {
    public OrderLine toOrderLine(OrderLineRequest orderLineRequest) {
        if (Objects.isNull(orderLineRequest)) {
            throw new NullPointerException("Input order line request value is null");
        }

        return OrderLine.builder()
                .id(orderLineRequest.id())
                .quantity(orderLineRequest.quantity())
                .order(
                        Order.builder()
                                .id(orderLineRequest.orderId())
                                .build())
                .build();
    }

    public OrderLineResponse toOrderLineResponse(OrderLine orderLine) {
        if (Objects.isNull(orderLine)) {
            throw new NullPointerException("Input order line value is null");
        }

        return new OrderLineResponse(
                orderLine.getId(),
                orderLine.getProductId(),
                orderLine.getQuantity(),
                orderLine.getOrder().getId()
        );
    }
}