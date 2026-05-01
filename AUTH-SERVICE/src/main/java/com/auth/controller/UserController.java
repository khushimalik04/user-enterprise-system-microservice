package com.auth.controller;

import com.auth.model.JwtTokenResponse;
import com.auth.model.LoginRequest;
import com.auth.model.User;
import com.auth.model.UserDto;
import com.auth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exposes registration and login endpoints for issuing JWTs.
 */
@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register-user")
    public UserDto registerUser(@RequestBody User user){
        return userService.saveUser(user);
    }

    @PostMapping("/generate-token")
    public JwtTokenResponse generateToken(@RequestBody LoginRequest loginRequest){

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            if(authentication.isAuthenticated()){
                // Token generation is delegated so signing stays in one place.
                return userService.generateToken(loginRequest.getUsername());
            }

        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Credentials");
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Credentials");
    }
}
