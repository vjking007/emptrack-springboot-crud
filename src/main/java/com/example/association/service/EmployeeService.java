package com.example.association.service;

import com.example.association.configuration.EmployeeMapper;
import com.example.association.dto.EmployeeRequest;
import com.example.association.dto.EmployeeResponse;
import com.example.association.entity.Employee;
import com.example.association.exception.ResourceNotFoundException;
import com.example.association.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepo;
    private final EmployeeMapper employeeMapper;

    @Transactional
    public List<EmployeeResponse> getAll() {
        return employeeRepo.findAll().stream()
                .peek(emp -> emp.getAddresses().size()) // force initialize
                .map(employeeMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public EmployeeResponse getById(int id) {
        Employee emp = employeeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        emp.getAddresses().size(); // Force LAZY load (Work only In Transactional)
        return employeeMapper.toResponse(emp);
    }

    public EmployeeResponse create(EmployeeRequest request) {
        Employee emp = employeeMapper.toEntity(request);
        emp  =employeeRepo.save(emp);

        return employeeMapper.toResponse(emp);
    }

    @Transactional //Mandatory
    public EmployeeResponse update(int id, EmployeeRequest request) {
        Employee emp = employeeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        employeeMapper.updateEntity(emp, request);
        return employeeMapper.toResponse(employeeRepo.save(emp));
    }

    public void delete(int id) {
        if (!employeeRepo.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with ID: " + id);
        }
        employeeRepo.deleteById(id);
    }
}
