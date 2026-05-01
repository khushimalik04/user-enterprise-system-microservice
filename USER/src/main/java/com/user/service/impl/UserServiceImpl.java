package com.user.service.impl;

import com.commonlib.exception.BadRequestException;
import com.commonlib.exception.ResourceNotFoundException;
import com.user.client.AddressClient;
import com.user.model.dto.AddressDto;
import com.user.model.dto.UserDto;
import com.user.model.entity.User;
import com.user.repository.UserRepository;
import com.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Handles persistence for users and enriches responses with address-service data when available.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AddressClient addressClient;
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${greeting}")
    private String greeting;

    @Value("${common.greeting}")
    private String commonGreeting;

    public UserServiceImpl(UserRepository userRepository,
                           ModelMapper modelMapper, AddressClient addressClient){
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.addressClient = addressClient;
    }


    @Override
    public UserDto saveUser(UserDto userDto) {
        if(userDto.getId() != null){
            throw new RuntimeException("User already exists");
        }
        User entity = modelMapper.map(userDto, User.class);
        entity.setCreatedAt(LocalDateTime.now());
        User savedEntity = userRepository.save(entity);
        return modelMapper.map(savedEntity, UserDto.class);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        if(id == null || userDto.getId() == null){
            throw new BadRequestException("Please provide employee id");
        }
        if(!Objects.equals(id, userDto.getId())){
            throw new BadRequestException("Id mismatch");
        }

        userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found with id: " + id, HttpStatus.NOT_FOUND));

        User entity = modelMapper.map(userDto, User.class);
        entity.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(entity);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public void deleteUser(Long id) {
        User user =  userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found with id: "+ id, HttpStatus.NOT_FOUND));
        userRepository.delete(user);
    }

    @Override
    public UserDto getSingleUser(Long id) {
        System.out.println("Greetings: "+ greeting);
        System.out.println("Common Greeting: "+ commonGreeting);
//        try {
//            Thread.sleep(6000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        User user =  userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not found with id: " + id, HttpStatus.NOT_FOUND));
        List<AddressDto> addresses = new ArrayList<>();
        UserDto dto =  modelMapper.map(user, UserDto.class);
        try{
            // Address data is optional from the perspective of the user response.
            addresses = addressClient.getAddressByUserId(user.getId());
            dto.setAddress(addresses);
        }catch(Exception e){
            log.error("Address not found with employee id: {}", user.getId());
        }
        return dto;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        if(users.isEmpty()){
            throw new ResourceNotFoundException("No users found", HttpStatus.NOT_FOUND);
        }
        List<UserDto> userDtoList =  users.stream().map(user -> modelMapper.map(user, UserDto.class)).toList();
        List<UserDto> response = new ArrayList<>();
        for(UserDto user : userDtoList){
            List<AddressDto> addresses = new ArrayList<>();
            try{
                // Each user is enriched independently so one address failure does not break the whole list.
                addresses = addressClient.getAddressByUserId(user.getId());
                user.setAddress(addresses);
            }catch(Exception e){
                log.error("Address not found with user id: {}", user.getId());
            }
            response.add(user);
        }
        return response;

    }

    @Override
    public UserDto getUserByUserCodeAndCompanyName(String userCode, String companyName) {
        User user = userRepository.findByUserCodeAndCompanyName(userCode, companyName).orElseThrow(()-> new ResourceNotFoundException("User not found with userCode: "
                + userCode + " and companyName: " + companyName, HttpStatus.NOT_FOUND));
        return modelMapper.map(user, UserDto.class);
    }
}
