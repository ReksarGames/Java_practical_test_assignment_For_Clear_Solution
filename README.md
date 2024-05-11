# RESTful API Implementation with Spring Boot

## Description
This project implements a RESTful API using Spring Boot for managing user resources. The API follows best practices and includes error handling, validation, and unit tests.

## Requirements
### Fields:
- Email (required) with email pattern validation
- First name (required)
- Last name (required)
- Birth date (required) with validation against current date
- Address (optional)
- Phone number (optional)

### Functionality:
- Create user (age validation based on configuration)
- Update user fields (partial or full)
- Delete user
- Search users by birth date range
- Unit Tests: Code coverage using Spring testing
- Error Handling: Proper error handling for REST requests
- Response Format: JSON format for API responses
- Persistence: Use of H2 database for data storage

## Technologies Used
- Spring Boot
- Spring MVC
- H2 Database
- Maven

## API Endpoints
1. **Create User**
   - **URL:** `/api/users`
   - **Method:** POST
   - **Request Body:**
     ```json
     {
       "email": "user@example.com",
       "firstName": "John",
       "lastName": "Doe",
       "birthDate": "1990-01-01",
       "address": "123 Main St",
       "phoneNumber": "1234567890"
     }
     ```
   - **Response:** User object

2. **Update User**
   - **URL:** `/api/users/{userId}`
   - **Method:** PUT
   - **Request Body:** Fields to update
   - **Response:** Updated User object

3. **Delete User**
   - **URL:** `/api/users/{userId}`
   - **Method:** DELETE
   - **Response:** Success message

4. **Search Users by Birth Date Range**
   - **URL:** `/api/users/search`
   - **Method:** GET
   - **Query Parameters:**
     - `from`: Start date (YYYY-MM-DD)
     - `to`: End date (YYYY-MM-DD)
   - **Response:** List of User objects

## Configuration
Age restriction for user creation is defined in the `application.properties` file.

## Testing
Unit tests cover all implemented functionality.

## Error Handling
Proper error responses are returned for invalid requests.
