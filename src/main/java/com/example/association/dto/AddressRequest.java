package com.example.association.dto;

import com.example.association.entity.AddressType;
import lombok.Data;

@Data
public class AddressRequest {

    private Integer addressId; // Optional, only present for updates
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private AddressType addressType;
}