package com.apiGateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.function.Predicate;

/**
 * Defines which public endpoints should bypass token validation at the gateway.
 */
@Component
public class Validator {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public static final List<String> endpoints = List.of(
            "/register-user",
            "/generate-token",
            "/validate-token/{token}"
    );

    public Predicate<ServerHttpRequest> predicate = serverHttpRequest -> {
        String requestPath = serverHttpRequest.getURI().getPath();
        // `true` means the request is protected and must pass through AuthFilter.
        return endpoints.stream().noneMatch(uri -> antPathMatcher.match(uri, requestPath));

    };

}
