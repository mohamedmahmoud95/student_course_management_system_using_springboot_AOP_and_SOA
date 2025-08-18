# 🎓 Student Course Management System (SCMS)

## 📋 Project Overview

The Student Course Management System (SCMS) is a comprehensive web application designed to manage student enrollments, course offerings, grade tracking, and administrative operations in an educational institution. Built using Spring Boot with a focus on **Aspect-Oriented Programming (AOP)** and **Service-Oriented Architecture (SOA)**, the system provides a robust, scalable, and maintainable solution for educational management.

### 🎯 Key Features

- **Student Management**: Registration, authentication, course enrollment, grade viewing
- **Course Management**: Course creation, capacity management, enrollment tracking
- **Administrative Operations**: Student oversight, grade management, enrollment approval
- **Real-time Notifications**: Student and admin notification systems
- **RESTful APIs**: Complete API documentation with Swagger
- **Responsive UI**: Modern Bootstrap-based interface for both admin and student portals

## 🏗️ Architecture Overview

### AOP (Aspect-Oriented Programming) Implementation
- **LoggingAspect**: Comprehensive method execution logging
- **PerformanceMonitoringAspect**: Execution time monitoring and performance alerts
- **SecurityAspect**: Authentication and authorization checks
- **TransactionManagementAspect**: Database transaction management
- **ExceptionHandlingAspect**: Centralized error handling

### SOA (Service-Oriented Architecture) Implementation
- **StudentService**: Student lifecycle management
- **CourseService**: Course operations and capacity management
- **EnrollmentService**: Enrollment workflow and status management
- **GradeService**: Grade recording and GPA calculation
- **NotificationService**: Student notification management
- **AdminNotificationService**: Administrative notification system
- **AdministratorService**: Administrative operations and reporting

## 🛠️ Technology Stack

### Backend
- **Spring Boot 3.5.4**: Main application framework
- **Spring Data JPA**: Object-relational mapping and data access
- **Spring Security**: Authentication and authorization
- **MySQL 8.0**: Relational database for persistent storage
- **Maven**: Build management and dependency resolution
- **Springdoc OpenAPI**: API documentation with Swagger UI

### Frontend
- **Thymeleaf**: Server-side templating engine
- **Bootstrap 5.3.0**: Responsive CSS framework
- **Font Awesome 6.0.0**: Icon library
- **JavaScript**: Client-side interactivity

### Development & Testing
- **JUnit 5**: Unit testing framework
- **Mockito**: Mocking framework for testing
- **Spring Boot Test**: Integration testing support

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

### Database Setup
1. Create MySQL database:
```sql
CREATE DATABASE scms_db_dev;
```

2. Create MySQL user:
```sql
CREATE USER 'scms_user'@'localhost' IDENTIFIED BY 'scms_password';
GRANT ALL PRIVILEGES ON scms_db_dev.* TO 'scms_user'@'localhost';
FLUSH PRIVILEGES;
```

### Application Setup
1. Clone the repository:
```bash
git clone <repository-url>
cd student-course-management-system
```

2. Build the application:
```bash
mvn clean compile
```

3. Run the application:
```bash
mvn spring-boot:run -Dspring.profiles.active=dev
```

4. Access the application:
- **Main Application**: http://localhost:8080
- **Swagger Documentation**: http://localhost:8080/swagger-ui.html

## 🔐 Testing Credentials

### Admin Access
- **Email**: admin@test.com
- **Password**: admin123

*Note: You may need to create an admin account first using the registration API*

### Student Access
- Students can register through the web interface
- Default password for new students: `password123`

## 📱 Application Features

### Admin Portal
- **Dashboard**: Overview of system statistics
- **Student Management**: View, add, edit, and delete students
- **Course Management**: Create and manage courses
- **Enrollment Management**: Approve/reject enrollment requests
- **Grade Management**: Record and manage student grades
- **Reports**: Generate enrollment and grade reports

### Student Portal
- **Dashboard**: Personal overview and current enrollments
- **Course Catalog**: Browse available courses
- **Enrollment**: Request enrollment in courses
- **Grades**: View personal grades and GPA
- **Notifications**: View system notifications

## 🔧 API Documentation

The complete API documentation is available through Swagger UI at:
**http://localhost:8080/swagger-ui.html**

### Key API Endpoints

#### Admin APIs
- `POST /api/admin/register` - Register new administrator
- `POST /api/admin/login` - Admin authentication
- `GET /api/admin/dashboard/stats` - Dashboard statistics
- `GET /api/admin/students` - List all students
- `GET /api/admin/courses` - List all courses
- `GET /api/admin/enrollments` - List all enrollments

#### Student APIs
- `POST /api/students/register` - Student registration
- `POST /api/students/login` - Student authentication
- `GET /api/students/{id}` - Get student details
- `PUT /api/students/{id}` - Update student information

#### Course APIs
- `POST /api/courses` - Create new course
- `GET /api/courses` - List all courses
- `GET /api/courses/available` - List available courses
- `PUT /api/courses/{id}` - Update course
- `DELETE /api/courses/{id}` - Delete course

#### Enrollment APIs
- `POST /api/enrollments/enroll` - Enroll student in course
- `POST /api/enrollments/withdraw` - Withdraw from course
- `GET /api/enrollments/student/{id}` - Get student enrollments
- `GET /api/enrollments/course/{id}` - Get course enrollments

#### Grade APIs
- `POST /api/grades/record` - Record student grade
- `GET /api/grades/student/{id}` - Get student grades
- `GET /api/grades/student/{id}/gpa` - Calculate student GPA
- `PUT /api/grades/{id}` - Update grade

## 🧪 Testing

Run the complete test suite:
```bash
mvn test
```

**Test Results**: All 47 tests pass successfully, covering:
- Application startup tests
- Controller layer tests
- Service layer tests
- Integration tests

## 📊 Database Schema

### Core Entities
- **Student**: Student information and authentication
- **Course**: Course details and capacity management
- **Enrollment**: Student-course relationships and status
- **Grade**: Academic performance records
- **Administrator**: Administrative user management
- **Notification**: Student notification system
- **AdminNotification**: Administrative notification system

## 🔒 Security Features

- **Password Encryption**: BCrypt password hashing
- **Session Management**: Secure session handling
- **Input Validation**: Comprehensive input sanitization
- **SQL Injection Prevention**: Parameterized queries
- **XSS Protection**: Output encoding and validation

## 📈 Performance Features

- **Database Connection Pooling**: HikariCP for optimal performance
- **Query Optimization**: Efficient JPA queries
- **Caching**: Strategic caching implementation
- **Performance Monitoring**: Real-time execution time tracking
- **Resource Management**: Optimized resource utilization

## 🚧 Implementation Challenges & Solutions

### 1. Circular Reference in JSON Serialization
**Challenge**: JPA entities with bidirectional relationships caused JSON serialization issues
**Solution**: Implemented `@JsonIgnore` annotations to prevent circular references

### 2. Password Authentication
**Challenge**: Plain text password comparison in authentication services
**Solution**: Integrated BCrypt password encoder for secure password verification

### 3. Database Migration from H2 to MySQL
**Challenge**: Reserved keyword conflicts and schema differences
**Solution**: Renamed conflicting columns and updated entity mappings

### 4. Test Data Management
**Challenge**: Maintaining clean test environment
**Solution**: Implemented conditional data initialization and cleanup scripts

## 🔮 Future Improvements

### Planned Enhancements
1. **Email Notifications**: Integration with email service providers
2. **File Upload**: Support for document and assignment submissions
3. **Advanced Reporting**: Enhanced analytics and reporting features
4. **Mobile Application**: Native mobile app development
5. **Real-time Chat**: Student-instructor communication system
6. **Calendar Integration**: Course scheduling and calendar management
7. **Payment Integration**: Course fee management system
8. **Multi-language Support**: Internationalization features

### Technical Improvements
1. **Microservices Architecture**: Service decomposition for scalability
2. **Container Orchestration**: Kubernetes deployment support
3. **Event-Driven Architecture**: Message queue integration
4. **Advanced Caching**: Redis integration for performance
5. **API Gateway**: Centralized API management
6. **Monitoring & Logging**: ELK stack integration

## 📞 Support & Contact

For technical support or questions about the Student Course Management System:

- **Documentation**: See `TECHNICAL_DOCUMENTATION.md` for detailed technical information
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **Issues**: Report bugs and feature requests through the project repository

## 📄 License

This project is developed for educational purposes as part of a university course requirement.

---

**🎓 Student Course Management System - A comprehensive solution for educational institution management**
