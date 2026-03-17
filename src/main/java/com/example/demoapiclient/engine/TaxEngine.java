package com.example.demoapiclient.engine;

import com.example.demoapiclient.dto.AppliedRuleDto;
import com.example.demoapiclient.entity.Employee;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class TaxEngine {

	private static final BigDecimal BASE_TAX_RATE = new BigDecimal("0.10");

	public TaxResult calculate(Employee employee, BigDecimal salary, BigDecimal commission) {
		if (commission == null) {
			commission = BigDecimal.ZERO;
		}

		List<AppliedRuleDto> appliedRules = new ArrayList<>();
		BigDecimal rateAdjustment = BigDecimal.ZERO;
		BigDecimal flatAdjustment = BigDecimal.ZERO;
		BigDecimal additionalTax = BigDecimal.ZERO;
		boolean zeroTaxOverride = false;
		BigDecimal zeroTaxCredit = BigDecimal.ZERO;
		BigDecimal zeroTaxExcessThreshold = null;

		Integer age = employee.getAge();
		Integer familySize = employee.getFamilySize() != null ? employee.getFamilySize() : 0;
		String dept = employee.getDepartment() != null ? employee.getDepartment().getName() : "";
		String school = employee.getSchool() != null ? employee.getSchool() : "";
		String location = employee.getLocation() != null ? employee.getLocation() : "";

		// Rule 1: Young Worker Discount — age < 25 -> -5% rate
		if (age != null && age < 25) {
			BigDecimal adj = new BigDecimal("-0.05");
			rateAdjustment = rateAdjustment.add(adj);
			BigDecimal impact = salary.multiply(adj).setScale(2, RoundingMode.HALF_UP);
			appliedRules.add(new AppliedRuleDto(1, "Young Worker Discount", "-5% rate", impact));
		}

		// Rule 2: Senior Surcharge — age > 60 -> +3% on excess > $50K
		if (age != null && age > 60 && salary.compareTo(new BigDecimal("50000")) > 0) {
			BigDecimal excess = salary.subtract(new BigDecimal("50000"));
			BigDecimal impact = excess.multiply(new BigDecimal("0.03")).setScale(2, RoundingMode.HALF_UP);
			additionalTax = additionalTax.add(impact);
			appliedRules.add(new AppliedRuleDto(2, "Senior Surcharge", "+3% on excess >$50K", impact));
		}

		// Rule 3: Large Family Relief — familySize >= 4 -> -$200/mo
		if (familySize >= 4) {
			BigDecimal impact = new BigDecimal("-200");
			flatAdjustment = flatAdjustment.add(impact);
			appliedRules.add(new AppliedRuleDto(3, "Large Family Relief", "-$200/mo", impact));
		}

		// Rule 4: Sales Commission Tax — dept Sales -> +10% on commission
		if ("Sales".equalsIgnoreCase(dept) && commission.compareTo(BigDecimal.ZERO) > 0) {
			BigDecimal impact = commission.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP);
			additionalTax = additionalTax.add(impact);
			appliedRules.add(new AppliedRuleDto(4, "Sales Commission Tax", "+10% on commission", impact));
		}

		// Rule 5: R&D Innovation Credit — dept R&D -> -$300/mo
		if ("R&D".equalsIgnoreCase(dept)) {
			BigDecimal impact = new BigDecimal("-300");
			flatAdjustment = flatAdjustment.add(impact);
			appliedRules.add(new AppliedRuleDto(5, "R&D Innovation Credit", "-$300/mo", impact));
		}

		// Rule 6: Rural Area Discount — location Rural -> -3% rate
		if ("Rural".equalsIgnoreCase(location)) {
			BigDecimal adj = new BigDecimal("-0.03");
			rateAdjustment = rateAdjustment.add(adj);
			BigDecimal impact = salary.multiply(adj).setScale(2, RoundingMode.HALF_UP);
			appliedRules.add(new AppliedRuleDto(6, "Rural Area Discount", "-3% rate", impact));
		}

		// Rule 7: Urban Zone Surcharge — location Tier-1 -> +4% tax
		if ("Tier-1".equalsIgnoreCase(location)) {
			BigDecimal adj = new BigDecimal("0.04");
			rateAdjustment = rateAdjustment.add(adj);
			BigDecimal impact = salary.multiply(adj).setScale(2, RoundingMode.HALF_UP);
			appliedRules.add(new AppliedRuleDto(7, "Urban Zone Surcharge", "+4% tax", impact));
		}

		// Rule 8: No Degree Low Income — No degree + salary < $35K -> 0% tax
		if ("No degree".equalsIgnoreCase(school) && salary.compareTo(new BigDecimal("35000")) < 0) {
			zeroTaxOverride = true;
			appliedRules.add(new AppliedRuleDto(8, "No Degree Low Income", "0% if <$35K", BigDecimal.ZERO));
		}

		// Rule 9: Young Urban Penalty — age < 25 + Tier-1 -> +2% net
		if (age != null && age < 25 && "Tier-1".equalsIgnoreCase(location)) {
			BigDecimal adj = new BigDecimal("0.02");
			rateAdjustment = rateAdjustment.add(adj);
			BigDecimal impact = salary.multiply(adj).setScale(2, RoundingMode.HALF_UP);
			appliedRules.add(new AppliedRuleDto(9, "Young Urban Penalty", "+2% net", impact));
		}

		// Rule 10: Elderly Rural Comfort — age > 60 + Rural -> -5% + $100/mo credit
		if (age != null && age > 60 && "Rural".equalsIgnoreCase(location)) {
			BigDecimal rateAdj = new BigDecimal("-0.05");
			rateAdjustment = rateAdjustment.add(rateAdj);
			BigDecimal flatAdj = new BigDecimal("-100");
			flatAdjustment = flatAdjustment.add(flatAdj);
			BigDecimal impact = salary.multiply(rateAdj).add(flatAdj).setScale(2, RoundingMode.HALF_UP);
			appliedRules.add(new AppliedRuleDto(10, "Elderly Rural Comfort", "-5% + $100/mo credit", impact));
		}

		// Rule 11: Mid-Career City Grind — age 35-50 + Tier-1 + salary > $90K -> +3%
		if (age != null && age >= 35 && age <= 50 && "Tier-1".equalsIgnoreCase(location)
				&& salary.compareTo(new BigDecimal("90000")) > 0) {
			BigDecimal adj = new BigDecimal("0.03");
			rateAdjustment = rateAdjustment.add(adj);
			BigDecimal impact = salary.multiply(adj).setScale(2, RoundingMode.HALF_UP);
			appliedRules.add(new AppliedRuleDto(11, "Mid-Career City Grind", "+3% if >$90K", impact));
		}

		// Rule 12: Young Rural Starter — age < 28 + Rural + salary < $40K -> 0% tax
		if (age != null && age < 28 && "Rural".equalsIgnoreCase(location)
				&& salary.compareTo(new BigDecimal("40000")) < 0) {
			zeroTaxOverride = true;
			appliedRules.add(new AppliedRuleDto(12, "Young Rural Starter", "0% if <$40K", BigDecimal.ZERO));
		}

		// Rule 13: Young Executive Fast Track — age < 30 + C-Suite -> +8% cap $500/mo
		if (age != null && age < 30 && "C-Suite".equalsIgnoreCase(dept)) {
			BigDecimal impact = salary.multiply(new BigDecimal("0.08")).setScale(2, RoundingMode.HALF_UP);
			if (impact.compareTo(new BigDecimal("500")) > 0) {
				impact = new BigDecimal("500.00");
			}
			additionalTax = additionalTax.add(impact);
			appliedRules.add(new AppliedRuleDto(13, "Young Executive Fast Track", "+8% cap $500/mo", impact));
		}

		// Rule 14: Senior R&D Expert — age > 55 + R&D -> -$500/mo
		if (age != null && age > 55 && "R&D".equalsIgnoreCase(dept)) {
			BigDecimal impact = new BigDecimal("-500");
			flatAdjustment = flatAdjustment.add(impact);
			appliedRules.add(new AppliedRuleDto(14, "Senior R&D Expert", "-$500/mo", impact));
		}

		// Rule 15: Mid-Age Sales Hustler — age 35-50 + Sales + commission > $5K -> +5% on commission > $5K
		if (age != null && age >= 35 && age <= 50 && "Sales".equalsIgnoreCase(dept)
				&& commission.compareTo(new BigDecimal("5000")) > 0) {
			BigDecimal excess = commission.subtract(new BigDecimal("5000"));
			BigDecimal impact = excess.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);
			additionalTax = additionalTax.add(impact);
			appliedRules.add(new AppliedRuleDto(15, "Mid-Age Sales Hustler", "+5% commission >$5K", impact));
		}

		// Rule 16: Young Support Relief — age < 27 + Support + salary < $38K -> -4% rate
		if (age != null && age < 27 && "Support".equalsIgnoreCase(dept)
				&& salary.compareTo(new BigDecimal("38000")) < 0) {
			BigDecimal adj = new BigDecimal("-0.04");
			rateAdjustment = rateAdjustment.add(adj);
			BigDecimal impact = salary.multiply(adj).setScale(2, RoundingMode.HALF_UP);
			appliedRules.add(new AppliedRuleDto(16, "Young Support Relief", "-4% if <$38K", impact));
		}

		// Rule 17: Urban Single Parent — familySize >= 2 + Tier-1 -> -$400/mo -5%
		if (familySize >= 2 && "Tier-1".equalsIgnoreCase(location)) {
			BigDecimal rateAdj = new BigDecimal("-0.05");
			rateAdjustment = rateAdjustment.add(rateAdj);
			BigDecimal flatAdj = new BigDecimal("-400");
			flatAdjustment = flatAdjustment.add(flatAdj);
			BigDecimal impact = salary.multiply(rateAdj).add(flatAdj).setScale(2, RoundingMode.HALF_UP);
			appliedRules.add(new AppliedRuleDto(17, "Urban Single Parent", "-$400/mo -5%", impact));
		}

		// Rule 18: Rural Large Family Bonus — familySize >= 4 + Rural -> -$350/mo
		if (familySize >= 4 && "Rural".equalsIgnoreCase(location)) {
			BigDecimal impact = new BigDecimal("-350");
			flatAdjustment = flatAdjustment.add(impact);
			appliedRules.add(new AppliedRuleDto(18, "Rural Large Family Bonus", "-$350/mo", impact));
		}

		// Rule 19: Suburban Family Balance — familySize 2-3 + Non-Tier1 + salary $50K-$80K -> -2%
		if (familySize >= 2 && familySize <= 3 && !"Tier-1".equalsIgnoreCase(location)
				&& salary.compareTo(new BigDecimal("50000")) >= 0
				&& salary.compareTo(new BigDecimal("80000")) <= 0) {
			BigDecimal adj = new BigDecimal("-0.02");
			rateAdjustment = rateAdjustment.add(adj);
			BigDecimal impact = salary.multiply(adj).setScale(2, RoundingMode.HALF_UP);
			appliedRules.add(new AppliedRuleDto(19, "Suburban Family Balance", "-2% if $50-80K", impact));
		}

		// Rule 20: No Dep Urban Dweller — familySize == 0 + Tier-1 + salary > $70K -> +3%
		if (familySize == 0 && "Tier-1".equalsIgnoreCase(location)
				&& salary.compareTo(new BigDecimal("70000")) > 0) {
			BigDecimal adj = new BigDecimal("0.03");
			rateAdjustment = rateAdjustment.add(adj);
			BigDecimal impact = salary.multiply(adj).setScale(2, RoundingMode.HALF_UP);
			appliedRules.add(new AppliedRuleDto(20, "No Dep Urban Dweller", "+3% if >$70K", impact));
		}

		// Rule 21: PhD in R&D Double Credit — R&D + PhD -> -$600/mo
		if ("R&D".equalsIgnoreCase(dept) && "PhD".equalsIgnoreCase(school)) {
			BigDecimal impact = new BigDecimal("-600");
			flatAdjustment = flatAdjustment.add(impact);
			appliedRules.add(new AppliedRuleDto(21, "PhD in R&D Double Credit", "-$600/mo", impact));
		}

		// Rule 22: MBA Executive Standard — C-Suite + Master's -> +4% on > $100K
		if ("C-Suite".equalsIgnoreCase(dept) && "Master's".equalsIgnoreCase(school)
				&& salary.compareTo(new BigDecimal("100000")) > 0) {
			BigDecimal excess = salary.subtract(new BigDecimal("100000"));
			BigDecimal impact = excess.multiply(new BigDecimal("0.04")).setScale(2, RoundingMode.HALF_UP);
			additionalTax = additionalTax.add(impact);
			appliedRules.add(new AppliedRuleDto(22, "MBA Executive Standard", "+4% on >$100K", impact));
		}

		// Rule 23: Vocational Sales Star — Sales + Vocational + salary > $55K -> -2%
		if ("Sales".equalsIgnoreCase(dept) && "Vocational".equalsIgnoreCase(school)
				&& salary.compareTo(new BigDecimal("55000")) > 0) {
			BigDecimal adj = new BigDecimal("-0.02");
			rateAdjustment = rateAdjustment.add(adj);
			BigDecimal impact = salary.multiply(adj).setScale(2, RoundingMode.HALF_UP);
			appliedRules.add(new AppliedRuleDto(23, "Vocational Sales Star", "-2% if >$55K", impact));
		}

		// Rule 24: Recent Grad in Support — Support + school contains "Grad" -> -$150/mo
		if ("Support".equalsIgnoreCase(dept) && school.toLowerCase().contains("grad")) {
			BigDecimal impact = new BigDecimal("-150");
			flatAdjustment = flatAdjustment.add(impact);
			appliedRules.add(new AppliedRuleDto(24, "Recent Grad in Support", "-$150/mo", impact));
		}

		// Rule 25: Young Family in City — age < 32 + familySize >= 2 + Tier-1 -> -$300/mo -3%
		if (age != null && age < 32 && familySize >= 2 && "Tier-1".equalsIgnoreCase(location)) {
			BigDecimal rateAdj = new BigDecimal("-0.03");
			rateAdjustment = rateAdjustment.add(rateAdj);
			BigDecimal flatAdj = new BigDecimal("-300");
			flatAdjustment = flatAdjustment.add(flatAdj);
			BigDecimal impact = salary.multiply(rateAdj).add(flatAdj).setScale(2, RoundingMode.HALF_UP);
			appliedRules.add(new AppliedRuleDto(25, "Young Family in City", "-$300/mo -3%", impact));
		}

		// Rule 26: Senior Parent Rural Ease — age > 58 + familySize >= 3 + Rural -> 0% up to $55K
		if (age != null && age > 58 && familySize >= 3 && "Rural".equalsIgnoreCase(location)) {
			if (salary.compareTo(new BigDecimal("55000")) <= 0) {
				zeroTaxOverride = true;
				appliedRules.add(new AppliedRuleDto(26, "Senior Parent Rural Ease", "0% up to $55K", BigDecimal.ZERO));
			} else {
				zeroTaxExcessThreshold = new BigDecimal("55000");
				BigDecimal savedTax = new BigDecimal("55000").multiply(BASE_TAX_RATE).setScale(2, RoundingMode.HALF_UP);
				appliedRules.add(new AppliedRuleDto(26, "Senior Parent Rural Ease", "0% up to $55K (taxed on excess)", savedTax.negate()));
			}
		}

		// Rule 27: Middle-Aged Suburban Parent — age 40-55 + familySize 1-3 + Non-Tier1 -> -1.5% -$100/mo
		if (age != null && age >= 40 && age <= 55 && familySize >= 1 && familySize <= 3
				&& !"Tier-1".equalsIgnoreCase(location)) {
			BigDecimal rateAdj = new BigDecimal("-0.015");
			rateAdjustment = rateAdjustment.add(rateAdj);
			BigDecimal flatAdj = new BigDecimal("-100");
			flatAdjustment = flatAdjustment.add(flatAdj);
			BigDecimal impact = salary.multiply(rateAdj).add(flatAdj).setScale(2, RoundingMode.HALF_UP);
			appliedRules.add(new AppliedRuleDto(27, "Middle-Aged Suburban Parent", "-1.5% -$100/mo", impact));
		}

		// Rule 28: Educated Single Urban Parent — familySize >= 1 + Master's/PhD + Tier-1 -> -$350/mo
		if (familySize >= 1 && "Tier-1".equalsIgnoreCase(location)
				&& ("Master's".equalsIgnoreCase(school) || "PhD".equalsIgnoreCase(school))) {
			BigDecimal impact = new BigDecimal("-350");
			flatAdjustment = flatAdjustment.add(impact);
			appliedRules.add(new AppliedRuleDto(28, "Educated Single Urban Parent", "-$350/mo", impact));
		}

		// Rule 29: Rural Vocational Large Family — familySize >= 4 + Vocational + Rural + salary < $50K -> 0%
		if (familySize >= 4 && "Vocational".equalsIgnoreCase(school)
				&& "Rural".equalsIgnoreCase(location) && salary.compareTo(new BigDecimal("50000")) < 0) {
			zeroTaxOverride = true;
			appliedRules.add(new AppliedRuleDto(29, "Rural Vocational Large Family", "0% if <$50K", BigDecimal.ZERO));
		}

		// Rule 30: Ultimate Hardship Exemption — age > 50 + familySize >= 3 + Support + No degree + Rural -> 0% + $200/mo credit
		if (age != null && age > 50 && familySize >= 3 && "Support".equalsIgnoreCase(dept)
				&& "No degree".equalsIgnoreCase(school) && "Rural".equalsIgnoreCase(location)) {
			zeroTaxOverride = true;
			zeroTaxCredit = zeroTaxCredit.add(new BigDecimal("200"));
			appliedRules.add(new AppliedRuleDto(30, "Ultimate Hardship Exemption", "0% + $200/mo credit", new BigDecimal("-200")));
		}

		// Calculate final tax
		BigDecimal tax;
		if (zeroTaxOverride) {
			tax = zeroTaxCredit.negate();
		} else if (zeroTaxExcessThreshold != null) {
			BigDecimal taxableAmount = salary.subtract(zeroTaxExcessThreshold);
			if (taxableAmount.compareTo(BigDecimal.ZERO) > 0) {
				BigDecimal effectiveRate = BASE_TAX_RATE.add(rateAdjustment);
				if (effectiveRate.compareTo(BigDecimal.ZERO) < 0) {
					effectiveRate = BigDecimal.ZERO;
				}
				tax = taxableAmount.multiply(effectiveRate).add(flatAdjustment).add(additionalTax);
			} else {
				tax = BigDecimal.ZERO;
			}
		} else {
			BigDecimal effectiveRate = BASE_TAX_RATE.add(rateAdjustment);
			if (effectiveRate.compareTo(BigDecimal.ZERO) < 0) {
				effectiveRate = BigDecimal.ZERO;
			}
			tax = salary.multiply(effectiveRate).add(flatAdjustment).add(additionalTax);
		}

		if (tax.compareTo(BigDecimal.ZERO) < 0 && !zeroTaxOverride) {
			tax = BigDecimal.ZERO;
		}
		tax = tax.setScale(2, RoundingMode.HALF_UP);

		BigDecimal net = salary.subtract(tax).setScale(2, RoundingMode.HALF_UP);

		return new TaxResult(salary, tax, net, appliedRules);
	}

	public static class TaxResult {

		private final BigDecimal grossSalary;
		private final BigDecimal tax;
		private final BigDecimal netSalary;
		private final List<AppliedRuleDto> appliedRules;

		public TaxResult(BigDecimal grossSalary, BigDecimal tax, BigDecimal netSalary, List<AppliedRuleDto> appliedRules) {
			this.grossSalary = grossSalary;
			this.tax = tax;
			this.netSalary = netSalary;
			this.appliedRules = appliedRules;
		}

		public BigDecimal getGrossSalary() { return grossSalary; }
		public BigDecimal getTax() { return tax; }
		public BigDecimal getNetSalary() { return netSalary; }
		public List<AppliedRuleDto> getAppliedRules() { return appliedRules; }
	}

}
