package com.user.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * API-facing user payload that can optionally include addresses fetched from another service.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long id;
    private String userName;
    private String userEmail;
    private String userCode;
    private String companyName;
    private List<AddressDto> address;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<AddressDto> getAddress() {
        return address;
    }

    public void setAddress(List<AddressDto> address) {
        // Null is allowed so responses can omit addresses when they were not requested or failed to load.
        this.address = address;
    }

    public UserDto(Long id, String userName, String userEmail, String userCode, String companyName) {
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userCode = userCode;
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userCode='" + userCode + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }
    public UserDto() {
    }


}
