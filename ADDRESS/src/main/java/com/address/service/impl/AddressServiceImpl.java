package com.address.service.impl;

import com.address.client.UserClient;
import com.address.exception.ResourceNotFoundException;
import com.address.model.dto.AddressDto;
import com.address.model.dto.AddressRequest;
import com.address.model.dto.AddressRequestDto;

import com.address.model.entity.Address;
import com.address.repository.AddressRepository;
import com.address.service.AddressService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Persists address records and keeps a user's address set in sync during updates.
 */
@Service
public class AddressServiceImpl implements AddressService {
    Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;
    private final UserClient userClient;

    @Value("${greeting}")
    private String greeting;

    @Value("${common.greeting}")
    private String commonGreeting;

    public AddressServiceImpl(AddressRepository addressRepository, ModelMapper modelMapper, UserClient userClient) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
        this.userClient = userClient;
    }

    @Override
    public List<AddressDto> saveAddress(AddressRequest addressRequest) {
        // Validate that the owning user exists before writing dependent records.
        userClient.getSingleUser(addressRequest.getUserId());
        List<Address> listToSave = this.saveOrUpdateAddressRequest(addressRequest);
        List<Address> savedAddress = addressRepository.saveAll(listToSave);
        return savedAddress.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();

    }

    @Override
    public List<AddressDto> updateAddress(AddressRequest addressRequest) {
        userClient.getSingleUser(addressRequest.getUserId());
        List<Address> addressByUserId =addressRepository.findAllByUserId(addressRequest.getUserId());
        if(addressByUserId.isEmpty()){
            log.info("No address found for user id {}", addressRequest.getUserId());
            log.info("Creating new address for user id {}", addressRequest.getUserId());
        }
        List<Address> listToUpdate = this.saveOrUpdateAddressRequest(addressRequest);

        List<Long> upcomingNonNullIds = listToUpdate.stream().map(Address::getId).filter(Objects::nonNull).toList();
        List<Long> existingIds = addressByUserId.stream().map(Address::getId).toList();

        // Any stored address missing from the incoming payload is treated as removed by the client.
        List<Long> idsToDelete = existingIds.stream().filter(id -> !upcomingNonNullIds.contains(id)).toList();
        if(!idsToDelete.isEmpty()){
            addressRepository.deleteAllById(idsToDelete);
        }
        List<Address> updatedAddress = addressRepository.saveAll(listToUpdate);
        return updatedAddress.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();
    }

    @Override
    public AddressDto getSingleAddress(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address not found with id:" + id, HttpStatus.NOT_FOUND));
        return modelMapper.map(address, AddressDto.class);
    }

    @Override
    public List<AddressDto> getAllAddresses() {
//        try {
//            Thread.sleep(6000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        List<Address> all = addressRepository.findAll();
        if(all.isEmpty()){
            throw new ResourceNotFoundException("No address found", HttpStatus.NOT_FOUND);
        }
        return all.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();
    }

    @Override
    public void deleteAddress(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Address not found with id:" + id, HttpStatus.NOT_FOUND));
        addressRepository.delete(address);

    }

    @Override
    public List<AddressDto> getAddressByUserId(Long userId) {
        System.out.println("Greeting: "+ greeting);
        System.out.println("Common Greeting: "+ commonGreeting);
        List<Address> addressByUserId = addressRepository.findAllByUserId(userId);
        if(addressByUserId.isEmpty()){
            throw new ResourceNotFoundException("No address found for user id: " + userId, HttpStatus.NOT_FOUND);
        }
        return addressByUserId.stream().map(address -> modelMapper.map(address, AddressDto.class)).toList();

    }

    private List<Address> saveOrUpdateAddressRequest(AddressRequest addressRequest) {
        List<Address> listToSave = new ArrayList<>();
        for (AddressRequestDto addressRequestDto : addressRequest.getAddressRequestDtoList()) {
            Address address = new Address();
            // Existing ids update rows; null ids create new address rows for the same user.
            address.setId(addressRequestDto.getId() != null ? addressRequestDto.getId() : null);
            address.setStreet(addressRequestDto.getStreet());
            address.setCity(addressRequestDto.getCity());
            address.setCountry(addressRequestDto.getCountry());
            address.setPinCode(addressRequestDto.getPinCode());
            address.setAddressType(addressRequestDto.getAddressType());
            address.setUserId(addressRequest.getUserId());
            listToSave.add(address);

        }
        return listToSave;
    }
}
