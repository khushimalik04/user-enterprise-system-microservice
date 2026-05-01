package com.apiGateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Simple fallback endpoints used when circuit breakers open for downstream services.
 */
@RestController
public class FallbackController {
    @GetMapping("/userServiceFallback")
    public Mono<String> userServiceFallbackMethod() {
        return Mono.just("User Service is currently unavailable. Please try again later.");
    }

    @GetMapping("/addressServiceFallback")
    public Mono<String> addressFallbackMethod(){
        return Mono.just("Address Service is down. Please try again later.");
    }
}
