package com.example.demoapiclient.repository;

import com.example.demoapiclient.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryRepository extends JpaRepository<Salary, Long> {
}
