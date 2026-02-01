# Java Spring Boot Development Instructions

## Purpose
Guidelines for developing and modifying Spring Boot applications in this project.

## Code Style and Standards

### Java Code
- Use Java 17+ features appropriately
- Follow Spring Boot best practices
- Use constructor injection over field injection
- Keep controllers thin, business logic in services
- Use DTOs for API requests/responses
- Apply proper validation annotations (@Valid, @NotNull, etc.)

### Naming Conventions
- Controllers: `*Controller.java`
- Services: `*Service.java`
- Repositories: `*Repository.java`
- DTOs: `*Request.java`, `*Response.java`
- Entities: No suffix, simple noun

### Error Handling
- Use @ControllerAdvice for global exception handling
- Create custom exceptions for business logic errors
- Return appropriate HTTP status codes
- Provide meaningful error messages in Korean for users

### Testing
- Write unit tests for services
- Write integration tests for controllers
- Use @SpringBootTest for integration tests
- Mock external dependencies
- Aim for >80% code coverage

### Logging
- Use SLF4J logger
- Log levels: ERROR for exceptions, WARN for recoverable issues, INFO for important events, DEBUG for detailed flow
- Include relevant context in log messages
- Avoid logging sensitive information

## Build and Dependencies

### Gradle
- Keep dependencies up to date
- Use version variables in gradle.properties
- Group related dependencies
- Document why specific versions are locked

### Configuration
- Use application.yaml for main configuration
- Use application-secret.yaml for sensitive data (excluded from git)
  - **IMPORTANT**: Ensure application-secret.yaml is listed in .gitignore
  - Provide application-secret.yaml.example with placeholder values for team reference
- Use profiles for different environments (dev, prod)
- Externalize configuration when possible

## Spring Boot Specific

### Controllers
- Use @RestController for REST APIs
- Use @Controller for view-based endpoints
- Apply @RequestMapping at class level for common path prefix
- Use appropriate HTTP methods (GET, POST, PUT, DELETE)
- Validate request parameters and body

### Services
- Mark with @Service annotation
- Keep methods focused and single-purpose
- Use @Transactional for database operations
- Handle exceptions at service level

### Data Access
- Use Spring Data JPA repositories
- Write custom queries using @Query when needed
- Use proper fetch strategies to avoid N+1 problems
- Apply database migrations with versioning

## File Upload Handling
- Validate file type and size before processing
- Stream large files instead of loading entirely into memory
- Clean up temporary files after processing
- Handle multipart configuration properly
- Set appropriate upload size limits in application.yaml

## Security
- Never commit secrets or credentials
- Use environment variables for sensitive data
- Validate and sanitize all user inputs
- Apply proper CORS configuration
- Use HTTPS in production
