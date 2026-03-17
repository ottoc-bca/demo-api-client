package com.example.demoapiclient.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "salary")
public class Salary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	@Column(name = "gross_salary", precision = 12, scale = 2)
	private BigDecimal grossSalary;

	@Column(precision = 12, scale = 2)
	private BigDecimal tax;

	@Column(name = "net_salary", precision = 12, scale = 2)
	private BigDecimal netSalary;

	@Column(name = "applied_rules", columnDefinition = "TEXT")
	private String appliedRules;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

}
