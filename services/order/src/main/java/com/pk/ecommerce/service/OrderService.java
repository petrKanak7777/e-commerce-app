package com.pk.ecommerce.service;

import com.pk.ecommerce.client.CustomerClient;
import com.pk.ecommerce.client.PaymentClient;
import com.pk.ecommerce.client.ProductClient;
import com.pk.ecommerce.error.Exception.BusinessException;
import com.pk.ecommerce.error.exception.ResourceNotFoundException;
import com.pk.ecommerce.kafka.producer.OrderProducer;
import com.pk.ecommerce.mapper.OrderMapper;
import com.pk.ecommerce.metric.OrderMetric;
import com.pk.ecommerce.model.entity.Order;
import com.pk.ecommerce.model.request.OrderRequest;
import com.pk.ecommerce.model.response.CustomerResponse;
import com.pk.ecommerce.model.response.OrderResponse;
import com.pk.ecommerce.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.lang.String.format;

@Slf4j
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
    private final OrderMetric orderMetric;

    public Integer createOrder(OrderRequest request) {
        orderMetric.createOrderTimerStart();

        var customer = getCustomer(request.customerId());
        var products = productClient.purchaseProducts(request.products());
        var order = saveOrder(request);

        orderLineService.saveOrderLines(request.products(), order.getId());
        paymentClient.requestOrderPayment(orderMapper.toPaymentRequest(request, order.getId(), customer));
        orderProducer.sendOrderConfirmation(orderMapper.toOrderConfirmation(request, customer, products));

        orderMetric.incApiCallCreateOrder();
        orderMetric.createOrderTimerStop();

        return order.getId();
    }

    public List<OrderResponse> findAll() {
        var orders = orderRepository.findAll()
                .stream()
                .map(orderMapper::toOrderResponse)
                .toList();
        log.info("INFO - Orders with size=[{}] was successfully returned", orders.size());
        return orders;
    }

    public OrderResponse findByOrderId(Integer orderId) {
        if (Objects.isNull(orderId)) {
            throw new NullPointerException("ERROR - orderId value is null");
        }

        var order = orderRepository.findById(orderId)
                .map(orderMapper::toOrderResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        format("ERROR - Cannot get order. Order with id=[%d] not found", orderId)
                ));
        log.info("INFO - Order with id=[{}] was successfully returned", order.id());
        return order;
    }

    private CustomerResponse getCustomer(String customerId) {
        var customer = customerClient.findByCustomerId(customerId)
                .orElseThrow(() -> new BusinessException(
                        format("ERROR - Cannot create order. Customer with id=[%s] not found", customerId)
                ));
        log.info("INFO - Customer with id=[{}] and email=[{}] was successfully returned", customer.id(), customer.email());
        return customer;
    }

    private Order saveOrder(OrderRequest request) {
        var order = orderRepository.save(orderMapper.toOrder(request));
        log.info("INFO - Order with id=[{}] was successfully saved", order.getId());
        return order;
    }
}