Employee Management System

1.A Spring Boot application that demonstrates CRUD operations for Employee and their associated Address entities using:
One-to-Many JPA mapping
DTOs and CustomMapper
Validation with Jakarta
Lazy loading + Manual initialization
Swagger API Docs
Layered architecture (Controller → Service → Repository)

2.Tech Stack
Java 17+
Spring Boot 3.x
Spring Data JPA
MSSQL (configurable)
Lombok
Custom Mapper
Jakarta Bean Validation
Springdoc OpenAPI (Swagger)

3.Features
Employee CRUD with embedded list of Address
@OneToMany with bidirectional mapping
Timestamps: createdAt, updatedAt
Lazy loading with manual control
Validation on request DTOs
Proper exception handling (ResourceNotFoundException, etc.)
Swagger UI for API Testing

4.API Endpoints
Method	        Endpoint	                Description
GET	         /api/employees	            Get all employees
GET	        /api/employees/{id}	        Get employee by ID
POST       /api/employees	            Create new employee
PUT	      /api/employees/{id}	        Update employee and their addresses
DELETE   /api/employees/{id}	        Delete employee by ID

5.Access Swagger UI
http://localhost:8080/swagger-ui/index.html

6.Sample POST /api/employees Payload
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phone": "1234567890",
  "hireDate": "2025-07-18",
  "department": "IT",
  "designation": "Developer",
  "addresses": [
    {
      "addressLine1": "123 Street",
      "city": "Delhi",
      "state": "Delhi",
      "postalCode": "110011",
      "country": "India",
      "addressType": "HOME"
    }
  ]
}

7.Validation
@NotBlank, @Email used in DTOs.
LocalDate passed as "yyyy-MM-dd" string in JSON.
@Valid used in controller to enforce validation

8.Lazy Loading Strategy
Default fetch is LAZY for addresses.
@Transactional used in service to manually trigger loading via emp.getAddresses().size()
