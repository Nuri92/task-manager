# Task Manager API

A RESTful Task Manager built with Spring Boot.

The application allows users to register, authenticate with JWT and manage their own tasks securely. Every task belongs to exactly one user, and users can only access their own data.

---

# Features

- User registration
- User login
- JWT authentication
- BCrypt password hashing
- User-specific task management
- Create tasks
- Read tasks
- Update tasks
- Delete tasks
- Toggle task status
- Bean Validation
- Global Exception Handling
- Integration Tests with MockMvc

---

# Technologies

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- PostgreSQL
- Docker Compose
- JWT (JSON Web Token)
- JUnit 5
- MockMvc
- Maven
- Git

---

# Architecture

```
                Client
     (Postman / Frontend)

               │
               ▼
       Spring Security
               │
               ▼
   JwtAuthenticationFilter
               │
               ▼
      DispatcherServlet
               │
               ▼
         Controllers
               │
               ▼
           Services
               │
               ▼
        Repositories
               │
               ▼
        Hibernate / JPA
               │
               ▼
          PostgreSQL
```

Project structure:

```
Controller
    │
    ▼
Service
    │
    ▼
Repository
    │
    ▼
Database
```

---

# Installation

Clone the repository:

```bash
git clone https://github.com/YOUR_USERNAME/task-manager.git
```

Navigate into the project:

```bash
cd task-manager
```

Start PostgreSQL:

```bash
docker compose up -d
```

Run the application:

```bash
mvn spring-boot:run
```

The API will be available at:

```
http://localhost:8080
```

---

# Authentication

Register a new user:

```
POST /auth/register
```

Login:

```
POST /auth/login
```

The response contains a JWT token:

```json
{
  "token": "eyJhbGc..."
}
```

Use the token for all protected endpoints:

```
Authorization: Bearer <your_token>
```

---

# API Endpoints

## Authentication

| Method | Endpoint | Description |
|----------|----------|-------------|
| POST | /auth/register | Register a new user |
| POST | /auth/login | Login user |

## Tasks

| Method | Endpoint | Description |
|----------|----------|-------------|
| GET | /tasks | Get all tasks of the authenticated user |
| GET | /tasks/{id} | Get task by id |
| POST | /tasks | Create task |
| PUT | /tasks/{id} | Update task |
| PATCH | /tasks/{id}/done | Toggle task status |
| DELETE | /tasks/{id} | Delete task |

---

# Security

The application uses:

- Spring Security
- JWT Authentication
- BCrypt password hashing

Every request (except `/auth/**`) requires a valid JWT.

Each task belongs to exactly one user.

Users cannot access tasks owned by another user.

---

# Testing

Integration tests are implemented using MockMvc.

Covered scenarios include:

- User registration
- Duplicate email registration
- User login
- Invalid password login
- Create task
- Update task
- Delete task
- Toggle task status
- Access protection between different users

Run all tests:

```bash
mvn test
```

---

# Project Structure

```
src
└── main
    └── java
        └── de.nuri.taskmanager
            ├── controller
            ├── service
            ├── repository
            ├── security
            ├── dto
            ├── entity
            ├── exception
            └── config
```

---

# Future Improvements

Possible future extensions:

- Refresh Tokens
- User roles (ADMIN / USER)
- Pagination
- Sorting
- Filtering
- Swagger / OpenAPI
- CI/CD Pipeline
- Docker image
- Frontend with React or Angular

---

# What I Learned

During this project I learned how to:

- build REST APIs with Spring Boot
- design layered applications
- use Spring Data JPA and Hibernate
- secure APIs with Spring Security and JWT
- hash passwords using BCrypt
- create DTOs
- validate requests
- implement Global Exception Handling
- write integration tests with MockMvc
- manage a project using Git and GitHub

---

# Author

Developed by Nuri as part of a Spring Boot learning project.