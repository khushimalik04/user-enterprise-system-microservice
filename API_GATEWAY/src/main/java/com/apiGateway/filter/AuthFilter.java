package com.apiGateway.filter;

import com.apiGateway.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * Rejects protected gateway requests that do not carry a valid bearer token.
 */
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private Validator validator;

    public AuthFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if(validator.predicate.test(exchange.getRequest())){
                // Only routes outside the allow-list reach this branch.
                HttpHeaders headers = exchange.getRequest().getHeaders();
                String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);

                if(authHeader == null){
                    throw new BadRequestException("Authorization header is missing", HttpStatus.UNAUTHORIZED);
                }
                String token = null;
                if(null != authHeader && authHeader.startsWith("Bearer ")){
                    token = authHeader.substring(7);

                }
                try{
                    // Validation is enough here because downstream services trust the gateway.
                    jwtUtil.validateToken(token);
                }catch (Exception e){
                    throw new BadRequestException("Invalid token", HttpStatus.UNAUTHORIZED);
                }
            }
            return chain.filter(exchange);
        };
    }

    public static class Config{

    }
}
