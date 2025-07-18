package com.example.association.dto;

import com.example.association.entity.AddressType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddressResponse {
    private Integer addressId;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private AddressType addressType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
