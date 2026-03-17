package com.example.demoapiclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryResponse {

	private Long salaryRecordId;

	private Long employeeId;

	private String employeeName;

	private BigDecimal grossSalary;

	private BigDecimal tax;

	private BigDecimal netSalary;

	private List<AppliedRuleDto> appliedRules;

}
