package com.example.demoapiclient.service;

import com.example.demoapiclient.dto.AppliedRuleDto;
import com.example.demoapiclient.dto.SalaryRequest;
import com.example.demoapiclient.dto.SalaryResponse;
import com.example.demoapiclient.engine.TaxEngine;
import com.example.demoapiclient.entity.Employee;
import com.example.demoapiclient.entity.Salary;
import com.example.demoapiclient.repository.EmployeeRepository;
import com.example.demoapiclient.repository.SalaryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SalaryService {

	private final EmployeeRepository employeeRepository;
	private final SalaryRepository salaryRepository;
	private final TaxEngine taxEngine;
	private final ObjectMapper objectMapper;

	public SalaryService(EmployeeRepository employeeRepository, SalaryRepository salaryRepository,
			TaxEngine taxEngine, ObjectMapper objectMapper) {
		this.employeeRepository = employeeRepository;
		this.salaryRepository = salaryRepository;
		this.taxEngine = taxEngine;
		this.objectMapper = objectMapper;
	}

	public Optional<SalaryResponse> calculateAndSave(Long employeeId, SalaryRequest request) {
		return employeeRepository.findById(employeeId).map(employee -> {
			TaxEngine.TaxResult result = taxEngine.calculate(employee, request.getAmount(), request.getCommission());

			Salary salary = new Salary();
			salary.setEmployee(employee);
			salary.setGrossSalary(result.getGrossSalary());
			salary.setTax(result.getTax());
			salary.setNetSalary(result.getNetSalary());
			salary.setCreatedAt(LocalDateTime.now());

			try {
				salary.setAppliedRules(objectMapper.writeValueAsString(result.getAppliedRules()));
			} catch (JsonProcessingException e) {
				salary.setAppliedRules("[]");
			}

			Salary saved = salaryRepository.save(salary);

			return new SalaryResponse(
					saved.getId(),
					employee.getId(),
					employee.getName(),
					result.getGrossSalary(),
					result.getTax(),
					result.getNetSalary(),
					result.getAppliedRules()
			);
		});
	}

}
