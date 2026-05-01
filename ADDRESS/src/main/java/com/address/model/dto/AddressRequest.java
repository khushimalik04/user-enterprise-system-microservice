package com.address.model.dto;

import java.util.List;

/**
 * Request body for creating or replacing the set of addresses owned by one user.
 */
public class AddressRequest {
    private Long userId;
    private List<AddressRequestDto> addressRequestDtoList;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<AddressRequestDto> getAddressRequestDtoList() {
        return addressRequestDtoList;
    }

    public void setAddressRequestDtoList(List<AddressRequestDto> addressRequestDtoList) {
        this.addressRequestDtoList = addressRequestDtoList;
    }
}
