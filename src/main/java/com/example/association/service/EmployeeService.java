package com.example.association.service;

import com.example.association.dto.AddressRequest;
import com.example.association.dto.EmployeeRequest;
import com.example.association.dto.EmployeeResponse;
import com.example.association.entity.Address;
import com.example.association.entity.Employee;
import com.example.association.exception.ResourceNotFoundException;
import com.example.association.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepo;
    private final ModelMapper mapper;

    public List<EmployeeResponse> getAll() {
        return employeeRepo.findAll().stream()
                .map(emp -> mapper.map(emp, EmployeeResponse.class))
                .collect(Collectors.toList());
    }

    public EmployeeResponse getById(int id) {
        Employee emp = employeeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        return mapper.map(emp, EmployeeResponse.class);
    }

    public EmployeeResponse create(EmployeeRequest request) {
        Employee emp = mapper.map(request, Employee.class);

        if (request.getAddresses() != null) {
            List<Address> addresses = request.getAddresses().stream()
                    .map(a -> {
                        Address address = mapper.map(a, Address.class);
                        address.setEmployee(emp);
                        return address;
                    }).collect(Collectors.toList());
            emp.setAddresses(addresses);
        }

        return mapper.map(employeeRepo.save(emp), EmployeeResponse.class);
    }

    public EmployeeResponse update(int id, EmployeeRequest request) {
        Employee emp = employeeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        emp.setFirstName(request.getFirstName());
        emp.setLastName(request.getLastName());
        emp.setEmail(request.getEmail());
        emp.setPhone(request.getPhone());
        emp.setDepartment(request.getDepartment());
        emp.setDesignation(request.getDesignation());

        // 🧠 Map of existing addresses
        Map<Integer, Address> existing = emp.getAddresses().stream()
                .collect(Collectors.toMap(Address::getAddressId, a -> a));

        List<Address> updatedAddresses = new ArrayList<>();

        for (AddressRequest addressRequest : request.getAddresses()) {
            if (addressRequest.getAddressId() != null && existing.containsKey(addressRequest.getAddressId())) {
                // 🔁 Update existing address
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
                Address newAddress = mapper.map(addressRequest, Address.class);
                newAddress.setEmployee(emp);
                updatedAddresses.add(newAddress);
            }
        }

        // Remove any old addresses not in the updated list
        Set<Integer> requestIds = request.getAddresses().stream()
                .map(AddressRequest::getAddressId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        emp.getAddresses().removeIf(a -> a.getAddressId() != null && !requestIds.contains(a.getAddressId()));

        // 🔄 Add/replace updated address list
        emp.getAddresses().clear();
        emp.getAddresses().addAll(updatedAddresses);

        return mapper.map(employeeRepo.save(emp), EmployeeResponse.class);
    }

    public void delete(int id) {
        if (!employeeRepo.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with ID: " + id);
        }
        employeeRepo.deleteById(id);
    }
}
