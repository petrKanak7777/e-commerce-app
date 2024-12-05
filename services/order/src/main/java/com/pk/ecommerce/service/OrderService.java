package com.pk.ecommerce.service;

import com.pk.ecommerce.client.CustomerClient;
import com.pk.ecommerce.client.ProductClient;
import com.pk.ecommerce.error.Exception.BusinessException;
import com.pk.ecommerce.error.exception.ResourceNotFoundException;
import com.pk.ecommerce.kafka.OrderConfirmation;
import com.pk.ecommerce.kafka.producer.OrderProducer;
import com.pk.ecommerce.mapper.OrderMapper;
import com.pk.ecommerce.model.request.OrderLineRequest;
import com.pk.ecommerce.model.request.OrderRequest;
import com.pk.ecommerce.model.request.PurchaseRequest;
import com.pk.ecommerce.model.response.CustomerResponse;
import com.pk.ecommerce.model.response.OrderResponse;
import com.pk.ecommerce.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;

    public Integer createOrder(OrderRequest request) {
        var customer = getCustomer(request.customerId());

        var purchaseProducts = productClient.purchaseProducts(request.products());

        var order = orderRepository.save(orderMapper.toOrder(request));

        saveOrderLines(request.products(), request.id());

        // todo: start payment process -> payment-ms

        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchaseProducts
                )
        );

        return order.getId();
    }

    private CustomerResponse getCustomer(String customerId) {
        return customerClient.getCustomerById(customerId)
                .orElseThrow(() -> new BusinessException(
                        format("Cannot create order. Customer with id=[%s] not found", customerId)
                ));
    }

    private void saveOrderLines(List<PurchaseRequest> products, Integer orderId) {
        // todo: here use orderLineService.saveOrderLines()
        for(PurchaseRequest purchaseRequest : products) {
            orderLineService.saveOrderLine(new OrderLineRequest(
                    null,
                    orderId,
                    purchaseRequest.productId(),
                    purchaseRequest.quantity()
            ));
        }
    }

    public List<OrderResponse> findAll() {
        log.info("call: /api/v1/orders");

        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    public OrderResponse findByOrderId(Integer orderId) {
        log.info("call: /api/v1/orders/{}", orderId);

        if (Objects.isNull(orderId)) {
            throw new NullPointerException("orderId value is null");
        }

        return orderRepository.findById(orderId)
                .map(orderMapper::toOrderResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        format("Cannot get order. Order with id=[%d] not found", orderId)
                ));
    }
}
