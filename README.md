# Test Task for Junior Java Developer

## Task Overview

You must implement an HTTP API server with basic CRUD operations for the Customer entity.

**Expected Result:** Link to the Git repository.

- **Backend:** Spring Boot application
- **Frontend:** Not needed
- **Database:** Any relational persistent SQL DB (MySQL/PostgreSQL/...)
- **Authentication:** Not needed

## Endpoints

### Create Customer

**POST** `/api/customers`

**Content-Type:** `application/json`

**Request Body:**
- `fullName`: String (2..50 chars including whitespaces)
- `email`: String (2..100 chars, unique, should include exactly one @)
- `phone`: String (6..14 chars, only digits, should start from +, optional field)

**Response Body:**
- `id`: Long
- `fullName`: String
- `email`: String
- `phone`: String

### Read All Customers

**GET** `/api/customers`

**Response Body:**
- List of:
    - `id`: Long
    - `fullName`: String
    - `email`: String
    - `phone`: String

### Read Customer

**GET** `/api/customers/{id}`

**Response Body:**
- `id`: Long
- `fullName`: String
- `email`: String
- `phone`: String

### Update Customer

**PUT** `/api/customers/{id}`

**Content-Type:** `application/json`

**Request Body:**
- `id`: Long
- `fullName`: String (2..50 chars including whitespaces)
- `email`: String (not editable)
- `phone`: String (6..14 chars, only digits, should start from +)

**Response Body:**
- `id`: Long
- `fullName`: String
- `email`: String
- `phone`: String

### Delete Customer

**DELETE** `/api/customers/{id}`

Just mark a customer as deleted, but leave his data in DB. Related DB column: `is_active`.

## Database Structure

**Customer Table:**
- `id`: bigint
- `created`: bigint
- `updated`: bigint
- `full_name`: varchar
- `email`: varchar
- `phone`: varchar, nullable
- `is_active`: bool
