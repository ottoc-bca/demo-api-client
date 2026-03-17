package com.example.demoapiclient.controller;

import com.example.demoapiclient.entity.Department;
import com.example.demoapiclient.service.DepartmentService;
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
@RequestMapping("/api/departments")
public class DepartmentController {

	private final DepartmentService departmentService;

	public DepartmentController(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@GetMapping
	public List<Department> getAll() {
		return departmentService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Department> getById(@PathVariable Long id) {
		return departmentService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping
	public ResponseEntity<Department> create(@RequestBody Department department) {
		Department saved = departmentService.save(department);
		return ResponseEntity.created(URI.create("/api/departments/" + saved.getId())).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Department> update(@PathVariable Long id, @RequestBody Department department) {
		return departmentService.update(id, department)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		if (departmentService.deleteById(id)) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

}
