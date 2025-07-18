package com.example.association.configuration;

import com.example.association.dto.AddressRequest;
import com.example.association.dto.AddressResponse;
import com.example.association.dto.EmployeeRequest;
import com.example.association.dto.EmployeeResponse;
import com.example.association.entity.Address;
import com.example.association.entity.Employee;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class EmployeeMapper {

    public EmployeeResponse toResponse(Employee emp) {
        EmployeeResponse res = new EmployeeResponse();

        res.setEmployeeId(emp.getEmployeeId());
        res.setFirstName(emp.getFirstName());
        res.setLastName(emp.getLastName());
        res.setEmail(emp.getEmail());
        res.setPhone(emp.getPhone());
        res.setDepartment(emp.getDepartment());
        res.setDesignation(emp.getDesignation());
        res.setHireDate(emp.getHireDate());
        res.setCreatedAt(emp.getCreatedAt());
        res.setUpdatedAt(emp.getUpdatedAt());

        if (emp.getAddresses() != null) {
            List<AddressResponse> addresses = emp.getAddresses().stream()
                    .map(this::toAddressResponse)
                    .collect(Collectors.toList());
            res.setAddressResponses(addresses);
        }

        return res;
    }

    public AddressResponse toAddressResponse(Address addr) {
        AddressResponse ar = new AddressResponse();
        ar.setAddressLine1(addr.getAddressLine1());
        ar.setAddressLine2(addr.getAddressLine2());
        ar.setCity(addr.getCity());
        ar.setState(addr.getState());
        ar.setCountry(addr.getCountry());
        ar.setPostalCode(addr.getPostalCode());
        ar.setAddressType(addr.getAddressType());
        ar.setCreatedAt(addr.getCreatedAt());
        ar.setUpdatedAt(addr.getUpdatedAt());
        return ar;
    }

    public Employee toEntity(EmployeeRequest req) {
        Employee emp = new Employee();
        emp.setFirstName(req.getFirstName());
        emp.setLastName(req.getLastName());
        emp.setEmail(req.getEmail());
        emp.setPhone(req.getPhone());
        emp.setHireDate(req.getHireDate());
        emp.setDepartment(req.getDepartment());
        emp.setDesignation(req.getDesignation());

        if (req.getAddresses() != null) {
            List<Address> addresses = req.getAddresses().stream()
                    .map(addr -> toAddressEntity(addr, emp))
                    .collect(Collectors.toList());
            emp.setAddresses(addresses);
        }

        return emp;
    }

    private Address toAddressEntity(AddressRequest req, Employee emp) {
        Address addr = new Address();
        addr.setAddressLine1(req.getAddressLine1());
        addr.setAddressLine2(req.getAddressLine2());
        addr.setCity(req.getCity());
        addr.setState(req.getState());
        addr.setCountry(req.getCountry());
        addr.setPostalCode(req.getPostalCode());
        addr.setAddressType(req.getAddressType());
        addr.setEmployee(emp); // important to set back-reference
        return addr;
    }

    public void updateEntity(Employee emp, EmployeeRequest request) {
        emp.setFirstName(request.getFirstName());
        emp.setLastName(request.getLastName());
        emp.setEmail(request.getEmail());
        emp.setPhone(request.getPhone());
        emp.setDepartment(request.getDepartment());
        emp.setDesignation(request.getDesignation());
        emp.setHireDate(request.getHireDate());

        // Map of existing addresses (by ID)
        Map<Integer, Address> existing = emp.getAddresses().stream()
                .collect(Collectors.toMap(Address::getAddressId, a -> a));

        List<Address> updatedAddresses = new ArrayList<>();

        for (AddressRequest addressRequest : request.getAddresses()) {
            if (addressRequest.getAddressId() != null && existing.containsKey(addressRequest.getAddressId())) {
                // Update existing
                Address address = existing.get(addressRequest.getAddressId());
                address.setAddressLine1(addressRequest.getAddressLine1());
                address.setAddressLine2(addressRequest.getAddressLine2());
                address.setCity(addressRequest.getCity());
                address.setState(addressRequest.getState());
                address.setPostalCode(addressRequest.getPostalCode());
                address.setCountry(addressRequest.getCountry());
                address.setAddressType(addressRequest.getAddressType());
                updatedAddresses.add(address);
            } else {
                // New address
                Address newAddress = new Address();
                newAddress.setAddressLine1(addressRequest.getAddressLine1());
                newAddress.setAddressLine2(addressRequest.getAddressLine2());
                newAddress.setCity(addressRequest.getCity());
                newAddress.setState(addressRequest.getState());
                newAddress.setPostalCode(addressRequest.getPostalCode());
                newAddress.setCountry(addressRequest.getCountry());
                newAddress.setAddressType(addressRequest.getAddressType());
                newAddress.setEmployee(emp); // Set back reference
                updatedAddresses.add(newAddress);
            }
        }

        // Remove addresses not present in request
        Set<Integer> requestIds = request.getAddresses().stream()
                .map(AddressRequest::getAddressId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        emp.getAddresses().removeIf(a -> a.getAddressId() != null && !requestIds.contains(a.getAddressId()));

        // Replace old list with new one
        emp.getAddresses().clear();
        emp.getAddresses().addAll(updatedAddresses);
    }
}
