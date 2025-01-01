package com.pk.ecommerce.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderMetric {
    private final MeterRegistry meterRegistry;

    private Counter apiCallCreateOrderCounter;

    private Timer createOrderTimer;
    private Timer.Sample createOrderTimerSample;


    public OrderMetric(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        registerApiCallCreateOrderCounter();
        registerCreateOrderTimer();
    }

    private void registerApiCallCreateOrderCounter() {
        apiCallCreateOrderCounter = Counter.builder("api_call_create_order_counter")
                .description("Number of Requests for createOrder")
                .register(meterRegistry);
        log.info("INFO - RegisterMetric - api_call_create_order_counter is registered");
    }

    private void registerCreateOrderTimer() {
        createOrderTimer = Timer.builder("api_create_order_timer")
                .description("Create order timer")
                .register(meterRegistry);
        log.info("INFO - RegisterMetric - api_create_order_timer is registered");
    }

    public void incApiCallCreateOrder() {
        apiCallCreateOrderCounter.increment();
    }

    public void createOrderTimerStart() {
        createOrderTimerSample = Timer.start(meterRegistry);
    }

    public void createOrderTimerStop() {
        createOrderTimerSample.stop(createOrderTimer);
    }
}