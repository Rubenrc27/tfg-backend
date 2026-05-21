# Project Instructions: Duck Surveys (tfg-backend)

This project is a survey management system ("Duck Surveys") built with Spring Boot.

## Tech Stack
- **Language:** Java 17
- **Framework:** Spring Boot 3.4.2
- **Security:** Spring Security (Form Login + JWT)
- **Persistence:** Spring Data JPA with MySQL
- **Templates:** Thymeleaf
- **Build Tool:** Maven

## Architecture
Standard Spring Boot architecture:
- `com.example.demo.controller`: REST and Web controllers.
- `com.example.demo.service`: Business logic.
- `com.example.demo.repository`: Data access.
- `com.example.demo.entity`: JPA entities (User, Survey, Question, Option, Response).
- `com.example.demo.dto`: Data Transfer Objects.
- `com.example.demo.config`: Security, Data Initialization, and JWT configuration.

## Security & Authentication
- **Admin Panel:** Uses Form Login.
- **REST API:** Uses JWT (`JwtAuthenticationFilter`).
- **Roles:**
  - `ROLE_ADMIN_SUPREMO`: Full access, including user management (`/admin/usuarios/**`).
  - `ROLE_ADMIN`: Access to admin panel (`/admin/**`).
  - `ROLE_USER`: Regular user access.
- **Public Paths:** `/`, `/css/**`, `/js/**`, `/images/**`, `/login`, `/api/auth/**`, `/api/surveys/**`.

## Development Commands
- **Run application:** `./mvnw spring-boot:run`
- **Build project:** `./mvnw clean install`
- **Tests:** `./mvnw test`

## Database Setup
- **Schema:** `BDTFG` (recommended).
- **Configuration:** Update `src/main/resources/application.properties` with your MySQL credentials.
- **Initialization:** The application includes a `DataInitializer` that populates the database with default users and surveys on startup.
  - **Default Admin:** `admin` / `admin123`

## Conventions
- Use Lombok for boilerplate code (getters, setters, etc.).
- Follow standard Java/Spring naming conventions.
- Keep business logic in Services, not Controllers.
- Document complex logic in `GEMINI.md` or as code comments.
