package com.example.association.repository;

import com.example.association.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

}
