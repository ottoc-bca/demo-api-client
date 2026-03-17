package com.example.demoapiclient.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalaryRequest {

	private BigDecimal amount;

	private BigDecimal commission;

}
