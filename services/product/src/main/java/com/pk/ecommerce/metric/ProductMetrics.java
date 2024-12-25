package com.pk.ecommerce.metric;

import com.pk.ecommerce.repository.CategoryRepository;
import com.pk.ecommerce.repository.ProductRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * Based on: https://medium.com/turkcell/adding-custom-metrics-in-spring-boot-application-for-prometheus-a3893ee86b61
 *           https://www.baeldung.com/java-prometheus-client
 */
@Slf4j
@Component
public class ProductMetrics {
    private final MeterRegistry meterRegistry;
    private Counter purchasedProductCounter;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductMetrics(
            MeterRegistry meterRegistry,
            ProductRepository productRepository,
            CategoryRepository categoryRepository) {
        this.meterRegistry = meterRegistry;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;

        registerApiCallPurchaseProductCounter();
        registerProductCount();
        registerCategoryCount();
    }

    private void registerApiCallPurchaseProductCounter() {
        purchasedProductCounter = Counter.builder("api_endpoint_purchaseProducts_counter")
                .description("Number of Requests for purchaseProducts")
                .register(meterRegistry);
        log.info("INFO - RegisterMetric - purchased_product_counter is registered");
    }

    private void registerProductCount() {
        Gauge.builder("api_product_count", getProductCount())
                .description("Products Count")
                .register(meterRegistry);
        log.info("INFO - RegisterMetric - api_product_count is registered");
    }

    private void registerCategoryCount() {
        Gauge.builder("api_category_count", getCategoryCount())
                .description("Category Count")
                .register(meterRegistry);
        log.info("INFO - RegisterMetric - api_category_count is registered");
    }

    private Supplier<Number> getProductCount() {
        return productRepository::count;
    }

    private Supplier<Number> getCategoryCount() {
        return categoryRepository::count;
    }

    public void incApiCallPurchasedProduct() {
        purchasedProductCounter.increment();
    }
}