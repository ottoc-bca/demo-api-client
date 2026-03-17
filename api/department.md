# Department API

Base URL: `http://localhost:8081/api/departments`

---

## List All Departments

### curl

```bash
curl -s http://localhost:8081/api/departments
```

### PowerShell

```powershell
Invoke-RestMethod -Uri http://localhost:8081/api/departments -Method Get
```

### Response

```json
[
  { "id": 1, "name": "HR" },
  { "id": 2, "name": "IT" },
  { "id": 3, "name": "Business" }
]
```

---

## Get Department by ID

### curl

```bash
curl -s http://localhost:8081/api/departments/1
```

### PowerShell

```powershell
Invoke-RestMethod -Uri http://localhost:8081/api/departments/1 -Method Get
```

### Response

```json
{ "id": 1, "name": "HR" }
```

---

## Create Department

### curl

```bash
curl -s -X POST http://localhost:8081/api/departments \
  -H "Content-Type: application/json" \
  -d '{"name": "Finance"}'
```

### PowerShell

```powershell
Invoke-RestMethod -Uri http://localhost:8081/api/departments -Method Post `
  -ContentType "application/json" `
  -Body '{"name": "Finance"}'
```

### Response (201 Created)

```json
{ "id": 4, "name": "Finance" }
```

---

## Update Department

### curl

```bash
curl -s -X PUT http://localhost:8081/api/departments/4 \
  -H "Content-Type: application/json" \
  -d '{"name": "Accounting"}'
```

### PowerShell

```powershell
Invoke-RestMethod -Uri http://localhost:8081/api/departments/4 -Method Put `
  -ContentType "application/json" `
  -Body '{"name": "Accounting"}'
```

### Response

```json
{ "id": 4, "name": "Accounting" }
```

---

## Delete Department

### curl

```bash
curl -s -X DELETE http://localhost:8081/api/departments/4 -w "%{http_code}"
```

### PowerShell

```powershell
Invoke-RestMethod -Uri http://localhost:8081/api/departments/4 -Method Delete
```

### Response

```
204 No Content
```
