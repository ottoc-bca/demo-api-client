package com.example.demoapiclient.controller;

import com.example.demoapiclient.entity.Employee;
import com.example.demoapiclient.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping
	public List<Employee> getAll() {
		return employeeService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Employee> getById(@PathVariable Long id) {
		return employeeService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Employee> create(@RequestBody Employee employee) {
		Employee saved = employeeService.save(employee);
		return ResponseEntity.created(URI.create("/api/employees/" + saved.getId())).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Employee> update(@PathVariable Long id, @RequestBody Employee employee) {
		return employeeService.update(id, employee)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		if (employeeService.deleteById(id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

}
