package com.example.demoapiclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppliedRuleDto {

	private int ruleNumber;

	private String ruleName;

	private String effect;

	private BigDecimal adjustment;

}
