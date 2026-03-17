package com.example.demoapiclient.repository;

import com.example.demoapiclient.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
