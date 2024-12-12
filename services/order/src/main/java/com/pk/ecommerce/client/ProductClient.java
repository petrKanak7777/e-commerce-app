package com.pk.ecommerce.client;

import com.pk.ecommerce.error.Exception.BusinessException;
import com.pk.ecommerce.model.request.PurchaseRequest;
import com.pk.ecommerce.model.response.ProductPurchaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * This client could be implemented like FeignClient.
 * But for educational purpose, we will implement it like RestTemplate.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductClient {

    @Value("${application.config.product-url}")
    private String productUrl;
    private final RestTemplate restTemplate;

    public List<ProductPurchaseResponse> purchaseProducts(List<PurchaseRequest> request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(CONTENT_TYPE, APPLICATION_JSON_VALUE);

        HttpEntity<List<PurchaseRequest>> requestEntity = new HttpEntity<>(request, headers);
        ParameterizedTypeReference<List<ProductPurchaseResponse>> responseType =
                new ParameterizedTypeReference<>() {};
        ResponseEntity<List<ProductPurchaseResponse>> responseEntity = restTemplate.exchange(
                productUrl + "/purchase",
                POST,
                requestEntity,
                responseType
        );
        if(responseEntity.getStatusCode().isError()) {
            throw new BusinessException("ERROR - An error occurred while processing the products purchase " + responseEntity.getStatusCode());
        }

        var purchasedProducts = responseEntity.getBody();
        if(purchasedProducts != null) {
            log.info("INFO - Purchased products with size=[{}] was successfully returned", purchasedProducts.size());
        }
        return purchasedProducts;
    }
}