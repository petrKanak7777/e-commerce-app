package com.pk.ecommerce.client;

import com.pk.ecommerce.model.request.PurchaseRequest;
import com.pk.ecommerce.model.response.ProductPurchaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "product-service",
        url = "${application.config.product-url}"
)
public interface ProductClient {
    @PostMapping("/purchase")
    List<ProductPurchaseResponse> purchaseProducts(
            @RequestBody List<PurchaseRequest> request);
}