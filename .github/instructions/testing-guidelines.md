# Testing Guidelines

## Purpose
Standards and best practices for writing tests in this project.

## Test Structure

### Unit Tests
- Location: `src/test/java` (mirror main package structure)
- Naming: `*Test.java` (e.g., `ExcelParseServiceTest.java`)
- Focus: Test single class in isolation
- Mock all dependencies using @Mock or @MockBean
- Use @InjectMocks or constructor injection for test subject

### Integration Tests
- Naming: `*IntegrationTest.java`
- Use @SpringBootTest
- Test multiple components working together
- May use real database (H2 in-memory for tests)
- Clean up test data after each test

### Test Naming Convention
- Method name should describe what is being tested
- Format: `methodName_scenario_expectedResult`
- Examples:
  - `parseExcel_validFile_returnsData()`
  - `parseExcel_invalidFormat_throwsException()`
  - `parseExcel_emptyFile_returnsEmptyList()`

## Test Coverage

### Minimum Requirements
- All service methods must have tests
- All controller endpoints must have tests
- Critical business logic must be tested thoroughly
- Edge cases and error conditions must be tested

### What to Test
- Happy path (valid inputs, expected outputs)
- Edge cases (empty, null, boundary values)
- Error conditions (invalid inputs, exceptions)
- Business rule validations
- Integration between components

## Mocking

### When to Mock
- External services (APIs, databases in unit tests)
- File I/O operations
- Time-dependent operations
- Complex dependencies

### How to Mock
```java
@Mock
private ExcelParseService excelParseService;

@InjectMocks
private FileUploadController controller;

@Test
void uploadFile_validExcel_returnsSuccess() {
    when(excelParseService.parse(any())).thenReturn(validData);
    // test controller
}
```

## Assertions

### Use Appropriate Assertions
- Use AssertJ or JUnit 5 assertions
- Be specific: `assertThat(result).isEqualTo(expected)` not just `assertTrue()`
- Test multiple aspects: value, type, size, content
- Use assertThrows for exception testing

### Examples
```java
assertThat(result).isNotNull();
assertThat(result.getRows()).hasSize(10);
assertThat(result.getErrors()).isEmpty();
assertThrows(InvalidFileException.class, () -> service.parse(invalidFile));
```

## Test Data

### Sample Files
- Store in `src/test/resources`
- Include various scenarios (valid, invalid, edge cases)
- Use realistic but anonymized data
- Document what each sample file tests

### Test Fixtures
- Create reusable test data builders
- Use @BeforeEach for common setup
- Clean up in @AfterEach
- Keep test data minimal but representative

## Running Tests

### Gradle Commands
```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests ExcelParseServiceTest

# Run with coverage report
./gradlew test jacocoTestReport

# Run only fast tests (unit tests)
./gradlew test -x integrationTest
```

### Test Reports
- Location: `build/reports/tests/test/index.html`
- Review failed tests immediately
- Check coverage reports: `build/reports/jacoco/test/html/index.html`

## Best Practices

### Do's
- Write tests first (TDD) when possible
- Keep tests simple and readable
- Test one thing per test method
- Use descriptive test names
- Clean up resources in tests
- Run tests before committing code

### Don'ts
- Don't test framework code (Spring, JPA, etc.)
- Don't depend on test execution order
- Don't use Thread.sleep() for timing
- Don't test implementation details
- Don't share mutable state between tests
- Don't skip writing tests for "simple" code

## Performance Testing

### Load Testing
- Test with large files (>1000 rows)
- Measure memory usage
- Test concurrent uploads
- Monitor response times

### Benchmarking
- Use JMH for micro-benchmarks
- Test critical paths (parsing algorithms)
- Compare before/after optimization

## Debugging Failed Tests

### Steps
1. Read error message carefully
2. Check test data and setup
3. Verify mocks are configured correctly
4. Run in debug mode with breakpoints
5. Check for environmental issues (file paths, timezones, etc.)
6. Review recent code changes

### Common Issues
- Timezone differences in date parsing
- File path separators (Windows vs Linux)
- Mock not configured for all scenarios
- Test data in wrong format
- Resource cleanup not happening
