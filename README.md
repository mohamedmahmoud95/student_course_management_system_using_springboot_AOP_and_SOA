# Student Course Management System (SCMS)

A comprehensive web-based application for managing student course registrations, grades, and academic records, demonstrating Aspect-Oriented Programming (AOP) and Service-Oriented Architecture (SOA) principles using the Spring Framework.

## Features

- **Student Management**: Registration, authentication, profile management
- **Course Management**: Course catalog, availability tracking, capacity management
- **Enrollment System**: Course registration and withdrawal functionality
- **Grade Management**: Grade recording, GPA calculation, academic reporting
- **Notification System**: Automated notifications for enrollment and grade updates
- **Administrative Tools**: Comprehensive reporting and system management

## Technical Stack

- **Framework**: Spring Boot 3.5.4
- **Database**: H2 (in-memory)
- **ORM**: Spring Data JPA
- **Security**: Spring Security with BCrypt password encoding
- **AOP**: Spring AOP with AspectJ
- **UI**: Thymeleaf templates with Bootstrap
- **API Documentation**: Swagger/OpenAPI
- **Build Tool**: Maven

## AOP Implementation

The application demonstrates five key aspects:

1. **Logging Aspect**: Automatically logs all method executions
2. **Security Aspect**: Validates user authentication and authorization
3. **Performance Monitoring Aspect**: Tracks method execution times
4. **Transaction Management Aspect**: Ensures data consistency
5. **Exception Handling Aspect**: Centralized error handling

## SOA Implementation

The system is built with loosely coupled services:

1. **Student Service**: Manages student profiles and authentication
2. **Course Service**: Handles course catalog and availability
3. **Enrollment Service**: Manages course registrations
4. **Grade Service**: Handles grade recording and calculations
5. **Notification Service**: Sends alerts and updates
6. **Administrator Service**: System administration and reporting

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### Accessing the Application

- **Main Application**: http://localhost:8080
- **Swagger API Documentation**: http://localhost:8080/swagger-ui.html
- **H2 Database Console**: http://localhost:8080/h2-console

### Demo Credentials

**Student Login:**
- Email: john@student.com
- Password: student123

**Admin Login:**
- Email: admin@scms.com
- Password: admin123

## API Endpoints

The application provides RESTful APIs for all operations:

- `/api/students/*` - Student management endpoints
- `/api/courses/*` - Course management endpoints
- `/api/enrollments/*` - Enrollment management endpoints
- `/api/grades/*` - Grade management endpoints
- `/api/admin/*` - Administrative endpoints

## Testing

Run the test suite:
```bash
mvn test
```

## Project Structure

```
src/
├── main/
│   ├── java/com/scms/
│   │   ├── aspect/          # AOP aspects
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # REST and web controllers
│   │   ├── entity/          # JPA entities
│   │   ├── repository/      # Data access layer
│   │   └── service/         # Business logic services
│   └── resources/
│       ├── templates/       # Thymeleaf templates
│       └── application.properties
└── test/                    # Unit and integration tests
```

## Key Features Demonstrated

- **AOP**: Cross-cutting concerns implemented as aspects
- **SOA**: Modular, loosely coupled service architecture
- **Security**: Role-based authentication and authorization
- **RESTful APIs**: Complete CRUD operations with proper HTTP status codes
- **Documentation**: Swagger/OpenAPI integration
- **Testing**: Comprehensive unit and integration tests
- **UI**: Modern, responsive web interface
