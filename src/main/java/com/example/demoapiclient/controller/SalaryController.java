package com.example.demoapiclient.controller;

import com.example.demoapiclient.dto.SalaryRequest;
import com.example.demoapiclient.dto.SalaryResponse;
import com.example.demoapiclient.service.SalaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/salary")
public class SalaryController {

	private final SalaryService salaryService;

	public SalaryController(SalaryService salaryService) {
		this.salaryService = salaryService;
	}

	@PostMapping("/{employeeId}")
	public ResponseEntity<SalaryResponse> calculateSalary(@PathVariable Long employeeId,
			@RequestBody SalaryRequest request) {
		return salaryService.calculateAndSave(employeeId, request)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

}
