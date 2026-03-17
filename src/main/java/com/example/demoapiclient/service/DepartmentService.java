package com.example.demoapiclient.service;

import com.example.demoapiclient.entity.Department;
import com.example.demoapiclient.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

	private final DepartmentRepository departmentRepository;

	public DepartmentService(DepartmentRepository departmentRepository) {
		this.departmentRepository = departmentRepository;
	}

	public List<Department> findAll() {
		return departmentRepository.findAll();
	}

	public Optional<Department> findById(Long id) {
		return departmentRepository.findById(id);
	}

	public Department save(Department department) {
		return departmentRepository.save(department);
	}

	public Optional<Department> update(Long id, Department department) {
		return departmentRepository.findById(id).map(existing -> {
			existing.setName(department.getName());
			return departmentRepository.save(existing);
		});
	}

	public boolean deleteById(Long id) {
		if (departmentRepository.existsById(id)) {
			departmentRepository.deleteById(id);
			return true;
		}
		return false;
	}

}
