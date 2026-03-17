# Salary / Tax Calculation API

Base URL: `http://localhost:8081/salary`

Base tax rate: **10%**. The engine evaluates 30 rules based on the employee's age, family size, department, education (school), and location. Multiple rules can stack. Zero-tax rules override all other calculations.

---

## Calculate Salary Tax

`POST /salary/{employeeId}`

### Request Body

| Field      | Type   | Required | Description                                      |
|------------|--------|----------|--------------------------------------------------|
| amount     | number | yes      | Gross salary amount                              |
| commission | number | no       | Commission amount (used for Sales dept rules)    |

### curl

```bash
curl -s -X POST http://localhost:8081/salary/1 \
  -H "Content-Type: application/json" \
  -d '{"amount": 5000}'
```

### curl (with commission, for Sales dept employees)

```bash
curl -s -X POST http://localhost:8081/salary/1 \
  -H "Content-Type: application/json" \
  -d '{"amount": 5000, "commission": 2000}'
```

### PowerShell

```powershell
Invoke-RestMethod -Uri http://localhost:8081/salary/1 -Method Post `
  -ContentType "application/json" `
  -Body '{"amount": 5000}'
```

### Response (200 OK)

```json
{
  "salaryRecordId": 1,
  "employeeId": 1,
  "employeeName": "John Doe",
  "grossSalary": 5000,
  "tax": 500.00,
  "netSalary": 4500.00,
  "appliedRules": [
    {
      "ruleNumber": 1,
      "ruleName": "Young Worker Discount",
      "effect": "-5% rate",
      "adjustment": -250.00
    }
  ]
}
```

### Response (404 Not Found)

Returned when the employee ID does not exist.

---

## Tax Rules Reference

| #  | Rule Name                      | Conditions                                      | Effect                    |
|----|--------------------------------|------------------------------------------------|---------------------------|
| 1  | Young Worker Discount          | age < 25                                       | -5% rate                  |
| 2  | Senior Surcharge               | age > 60, salary > $50K                        | +3% on excess > $50K      |
| 3  | Large Family Relief            | familySize >= 4                                | -$200/mo                  |
| 4  | Sales Commission Tax           | dept = Sales, commission > 0                   | +10% on commission        |
| 5  | R&D Innovation Credit          | dept = R&D                                     | -$300/mo                  |
| 6  | Rural Area Discount            | location = Rural                               | -3% rate                  |
| 7  | Urban Zone Surcharge           | location = Tier-1                              | +4% tax                   |
| 8  | No Degree Low Income           | school = No degree, salary < $35K              | 0% tax                    |
| 9  | Young Urban Penalty            | age < 25, location = Tier-1                    | +2% net                   |
| 10 | Elderly Rural Comfort          | age > 60, location = Rural                     | -5% + $100/mo credit      |
| 11 | Mid-Career City Grind          | age 35-50, location = Tier-1, salary > $90K    | +3%                       |
| 12 | Young Rural Starter            | age < 28, location = Rural, salary < $40K      | 0% tax                    |
| 13 | Young Executive Fast Track     | age < 30, dept = C-Suite                       | +8% cap $500/mo           |
| 14 | Senior R&D Expert              | age > 55, dept = R&D                           | -$500/mo                  |
| 15 | Mid-Age Sales Hustler          | age 35-50, dept = Sales, commission > $5K      | +5% on commission > $5K   |
| 16 | Young Support Relief           | age < 27, dept = Support, salary < $38K        | -4% rate                  |
| 17 | Urban Single Parent            | familySize >= 2, location = Tier-1             | -$400/mo -5%              |
| 18 | Rural Large Family Bonus       | familySize >= 4, location = Rural              | -$350/mo                  |
| 19 | Suburban Family Balance        | familySize 2-3, location != Tier-1, $50-80K    | -2%                       |
| 20 | No Dep Urban Dweller           | familySize = 0, location = Tier-1, > $70K      | +3%                       |
| 21 | PhD in R&D Double Credit       | dept = R&D, school = PhD                       | -$600/mo                  |
| 22 | MBA Executive Standard         | dept = C-Suite, school = Master's, > $100K     | +4% on > $100K            |
| 23 | Vocational Sales Star          | dept = Sales, school = Vocational, > $55K      | -2%                       |
| 24 | Recent Grad in Support         | dept = Support, school contains "Grad"         | -$150/mo                  |
| 25 | Young Family in City           | age < 32, familySize >= 2, Tier-1              | -$300/mo -3%              |
| 26 | Senior Parent Rural Ease       | age > 58, familySize >= 3, Rural               | 0% up to $55K             |
| 27 | Middle-Aged Suburban Parent    | age 40-55, familySize 1-3, non-Tier-1          | -1.5% -$100/mo            |
| 28 | Educated Single Urban Parent   | familySize >= 1, Master's/PhD, Tier-1          | -$350/mo                  |
| 29 | Rural Vocational Large Family  | familySize >= 4, Vocational, Rural, < $50K     | 0% tax                    |
| 30 | Ultimate Hardship Exemption    | age > 50, familySize >= 3, Support, No degree, Rural | 0% + $200/mo credit |

### Notes

- Multiple rules can apply simultaneously; their effects stack.
- Zero-tax rules (8, 12, 26, 29, 30) override all other rate/flat adjustments when triggered.
- Rule 30 provides a $200/mo credit (negative tax), meaning net salary > gross salary.
- The `commission` field in the request is only used by rules 4 and 15 (Sales department).
- Each calculation is persisted to the `salary` table with gross, tax, net, and applied rules as JSON.
