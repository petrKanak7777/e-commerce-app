package com.pk.ecommerce.config;

import brave.Tracing;
import brave.http.HttpTracing;
import brave.spring.web.TracingClientHttpRequestInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public HttpTracing create() {
        return HttpTracing
                .newBuilder(Tracing.current())
                .build();
    }

    @Bean
    public RestTemplate restTemplate(final HttpTracing httpTracing) {
        return new RestTemplateBuilder()
                .interceptors(TracingClientHttpRequestInterceptor.create(httpTracing))
                .build();
    }
}