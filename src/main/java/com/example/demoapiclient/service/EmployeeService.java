package com.example.demoapiclient.service;

import com.example.demoapiclient.entity.Employee;
import com.example.demoapiclient.repository.EmployeeRepository;
import com.example.demoapiclient.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final DepartmentRepository departmentRepository;

	public EmployeeService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
		this.employeeRepository = employeeRepository;
		this.departmentRepository = departmentRepository;
	}

	public List<Employee> findAll() {
		return employeeRepository.findAll();
	}

	public Optional<Employee> findById(Long id) {
		return employeeRepository.findById(id);
	}

	public Employee save(Employee employee) {
		if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
			employee.setDepartment(
					departmentRepository.findById(employee.getDepartment().getId())
							.orElse(null));
		}
		return employeeRepository.save(employee);
	}

	public Optional<Employee> update(Long id, Employee employee) {
		return employeeRepository.findById(id).map(existing -> {
			existing.setName(employee.getName());
			if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
				existing.setDepartment(
						departmentRepository.findById(employee.getDepartment().getId())
								.orElse(null));
			}
			return employeeRepository.save(existing);
		});
	}

	public boolean deleteById(Long id) {
		if (employeeRepository.existsById(id)) {
			employeeRepository.deleteById(id);
			return true;
		}
		return false;
	}

}
