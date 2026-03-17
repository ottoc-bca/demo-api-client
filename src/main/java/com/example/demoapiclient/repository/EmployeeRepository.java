package com.example.demoapiclient.repository;

import com.example.demoapiclient.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
