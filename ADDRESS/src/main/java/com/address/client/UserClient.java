package com.address.client;

import com.address.model.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client used to verify or retrieve the owning user before address operations.
 */
@FeignClient(name = "USER")
public interface UserClient {
    @GetMapping("/users/{id}")
    UserDto getSingleUser(@PathVariable Long id);

}
