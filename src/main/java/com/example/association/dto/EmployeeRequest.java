package com.example.association.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EmployeeRequest {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    @NotBlank
    private String email;

    private String phone;
    @NotNull
    private LocalDate hireDate;
    private String department;
    private String designation;
    private List<AddressRequest> addresses;
}