package com.pk.ecommerce.service;

import com.pk.ecommerce.client.CustomerClient;
import com.pk.ecommerce.client.PaymentClient;
import com.pk.ecommerce.client.ProductClient;
import com.pk.ecommerce.error.Exception.BusinessException;
import com.pk.ecommerce.error.exception.ResourceNotFoundException;
import com.pk.ecommerce.kafka.producer.OrderProducer;
import com.pk.ecommerce.mapper.OrderMapper;
import com.pk.ecommerce.model.request.OrderRequest;
import com.pk.ecommerce.model.response.CustomerResponse;
import com.pk.ecommerce.model.response.OrderResponse;
import com.pk.ecommerce.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

@Service
@AllArgsConstructor
public class OrderService {

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

    public Integer createOrder(OrderRequest request) {
        var customer = getCustomer(request.customerId());
        var products = productClient.purchaseProducts(request.products());
        var order = orderRepository.save(orderMapper.toOrder(request));

        orderLineService.saveOrderLines(request.products(), order.getId());
        paymentClient.requestOrderPayment(orderMapper.toPaymentRequest(request, order.getId(), customer));
        orderProducer.sendOrderConfirmation(orderMapper.toOrderConfirmation(request, customer, products));

        return order.getId();
    }

    private CustomerResponse getCustomer(String customerId) {
        return customerClient.getCustomerById(customerId)
                .orElseThrow(() -> new BusinessException(
                        format("Cannot create order. Customer with id=[%s] not found", customerId)
                ));
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    public OrderResponse findByOrderId(Integer orderId) {
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