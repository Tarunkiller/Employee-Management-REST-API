# Employee Management REST API

A production-ready Java backend REST API built with Spring Boot, Hibernate/JPA, and MySQL.
Implements JWT-based authentication, role-based access control, and clean MVC architecture.

## Tech Stack
Java 17 · Spring Boot 3 · Hibernate/JPA · MySQL 8 · Maven · JWT · JUnit 5

## Features
- JWT Auth with role-based access (ADMIN / HR / EMPLOYEE)
- Full employee CRUD with soft-delete
- Department management with employee count
- Pagination and search filtering
- Normalized 6-table MySQL schema
- Global exception handling with consistent error responses
- 20+ REST endpoints documented via Swagger UI

## Setup
1. Clone repo and open in IntelliJ IDEA
2. Create MySQL database: `CREATE DATABASE emp_management;`
3. Update `application.properties` with your DB credentials
4. Run: `mvn spring-boot:run`
5. API Docs: http://localhost:8080/swagger-ui.html

## API Overview
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | /api/auth/register | Public |
| POST | /api/auth/login | Public |
| GET | /api/employees | ADMIN, HR |
| POST | /api/employees | ADMIN, HR |
| PUT | /api/employees/{id} | ADMIN |
| DELETE | /api/employees/{id} | ADMIN |
| GET | /api/departments | All |
