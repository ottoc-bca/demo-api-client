# Employee API

Base URL: `http://localhost:8081/api/employees`

---

## List All Employees

### curl

```bash
curl -s http://localhost:8081/api/employees
```

### PowerShell

```powershell
Invoke-RestMethod -Uri http://localhost:8081/api/employees -Method Get
```

### Response

```json
[
  {
    "id": 1,
    "name": "John Doe",
    "age": 30,
    "familySize": 4,
    "school": "MIT",
    "location": "Jakarta",
    "department": { "id": 2, "name": "IT" }
  }
]
```

---

## Get Employee by ID

### curl

```bash
curl -s http://localhost:8081/api/employees/1
```

### PowerShell

```powershell
Invoke-RestMethod -Uri http://localhost:8081/api/employees/1 -Method Get
```

### Response

```json
{
  "id": 1,
  "name": "John Doe",
  "age": 30,
  "familySize": 4,
  "school": "MIT",
  "location": "Jakarta",
  "department": { "id": 2, "name": "IT" }
}
```

---

## Create Employee

### curl

```bash
curl -s -X POST http://localhost:8081/api/employees \
  -H "Content-Type: application/json" \
  -d '{"name": "John Doe", "age": 30, "familySize": 4, "school": "MIT", "location": "Jakarta", "department": {"id": 2}}'
```

### PowerShell

```powershell
Invoke-RestMethod -Uri http://localhost:8081/api/employees -Method Post `
  -ContentType "application/json" `
  -Body '{"name": "John Doe", "age": 30, "familySize": 4, "school": "MIT", "location": "Jakarta", "department": {"id": 2}}'
```

### Response (201 Created)

```json
{
  "id": 1,
  "name": "John Doe",
  "age": 30,
  "familySize": 4,
  "school": "MIT",
  "location": "Jakarta",
  "department": { "id": 2, "name": "IT" }
}
```

---

## Update Employee

### curl

```bash
curl -s -X PUT http://localhost:8081/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{"name": "Jane Doe", "age": 28, "familySize": 3, "school": "UI", "location": "Bandung", "department": {"id": 1}}'
```

### PowerShell

```powershell
Invoke-RestMethod -Uri http://localhost:8081/api/employees/1 -Method Put `
  -ContentType "application/json" `
  -Body '{"name": "Jane Doe", "age": 28, "familySize": 3, "school": "UI", "location": "Bandung", "department": {"id": 1}}'
```

### Response

```json
{
  "id": 1,
  "name": "Jane Doe",
  "age": 28,
  "familySize": 3,
  "school": "UI",
  "location": "Bandung",
  "department": { "id": 1, "name": "HR" }
}
```

---

## Delete Employee

### curl

```bash
curl -s -X DELETE http://localhost:8081/api/employees/1 -w "%{http_code}"
```

### PowerShell

```powershell
Invoke-RestMethod -Uri http://localhost:8081/api/employees/1 -Method Delete
```

### Response

```
204 No Content
```
