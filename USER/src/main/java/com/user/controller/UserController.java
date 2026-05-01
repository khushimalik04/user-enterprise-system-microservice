package com.user.controller;


import com.commonlib.exception.MissingParameterException;
import com.user.model.dto.UserDto;
import com.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CRUD endpoints for user records plus a filtered lookup by business keys.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/save")
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto){
        UserDto response = userService.saveUser(userDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable Long id ){
        UserDto response = userService.updateUser(id, userDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getSingleUser(@PathVariable Long id){
        UserDto response = userService.getSingleUser(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<UserDto>> getAllUsers(){
        Iterable<UserDto> response = userService.getAllUsers();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/get-by-user-code-and-company-name")
    public ResponseEntity<UserDto> getUserByUserCodeAndCompanyName(@RequestParam(required = false) String userCode,
                                                                   @RequestParam(required = false) String companyName){
        List<String> missingParameters = new ArrayList<>();
        if(userCode == null || userCode.trim().isEmpty()){
            missingParameters.add("userCode");
        }
        if(companyName == null || companyName.trim().isEmpty()){
            missingParameters.add("companyName");
        }
        if(!missingParameters.isEmpty()){
            // Report every missing query parameter in one response instead of failing one-by-one.
            String finalMessage = missingParameters.stream().collect(Collectors.joining(","));
            throw new MissingParameterException("Please provide " + finalMessage);
        }

        UserDto response = userService.getUserByUserCodeAndCompanyName(userCode, companyName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }





}
