# Refactoring Prompt

## Purpose
Guide AI to perform safe and effective code refactoring while maintaining functionality.

## Instructions
When refactoring code, follow this systematic approach:

### 1. Identify Refactoring Target
- [ ] Code smells (long methods, large classes, duplicated code)
- [ ] Low maintainability areas
- [ ] Performance bottlenecks
- [ ] Hard-to-test code
- [ ] Outdated patterns or practices

### 2. Establish Safety Net
- [ ] Ensure adequate test coverage exists
- [ ] Add tests if coverage is insufficient
- [ ] Run all tests and ensure they pass
- [ ] Commit current working state

### 3. Refactoring Goals
Define clear objectives:
- [ ] Improve readability
- [ ] Reduce complexity
- [ ] Eliminate duplication
- [ ] Enhance testability
- [ ] Improve performance
- [ ] Better align with design patterns
- [ ] Update to modern practices

### 4. Refactoring Techniques

#### Extract Method
Break down large methods into smaller, focused ones
```java
// Before
public void processData(File file) {
    // 50 lines of code
}

// After
public void processData(File file) {
    Object source = loadSource(file);
    List<Data> data = parseData(source);
    validateData(data);
    saveData(data);
}
```

#### Extract Class
Separate responsibilities into different classes
```java
// Before: DataService handles everything

// After: 
// - DataReader (reading)
// - DataParser (parsing)
// - DataValidator (validation)
```

#### Rename
Use meaningful names
```java
// Before
List<String> list1 = new ArrayList<>();

// After
List<String> columnNames = new ArrayList<>();
```

#### Introduce Parameter Object
Group related parameters
```java
// Before
void createReport(String title, Date start, Date end, String format)

// After
void createReport(ReportParameters params)
```

#### Replace Conditional with Polymorphism
```java
// Before: Multiple if-else or switch

// After: Strategy pattern or inheritance
```

### 5. Step-by-Step Process
- [ ] Make one small change at a time
- [ ] Run tests after each change
- [ ] Commit after each successful refactoring
- [ ] Follow project commit message guidelines

### 6. Code Quality Checks
After refactoring, verify:
- [ ] All tests still pass
- [ ] No new warnings or errors
- [ ] Code coverage maintained or improved
- [ ] Performance not degraded
- [ ] API contracts unchanged (if public)
- [ ] Documentation updated if needed

### 7. Common Refactoring Patterns

#### Long Method
- Extract smaller methods
- Use meaningful method names
- Each method should do one thing

#### Large Class
- Extract classes for separate concerns
- Apply Single Responsibility Principle

#### Duplicated Code
- Extract common code to methods or classes
- Use inheritance or composition

#### Long Parameter List
- Introduce parameter object
- Use builder pattern

#### Primitive Obsession
- Create value objects
- Use enums for type-safe constants

### 8. Framework-Specific Refactoring

#### Replace Field Injection with Constructor Injection
```java
// Before
@Autowired
private DataService dataService;

// After (constructor injection)
private final DataService dataService;

public Controller(DataService dataService) {
    this.dataService = dataService;
}
```

#### Extract Configuration
```java
// Before: Hard-coded values

// After: Externalized configuration
@Value("${app.max-file-size}")
private long maxFileSize;
```

## Refactoring Checklist

### Before Starting
- [ ] Understand the current code
- [ ] Review related tests
- [ ] Identify code smells
- [ ] Plan refactoring steps
- [ ] Create git branch for refactoring

### During Refactoring
- [ ] Make incremental changes
- [ ] Run tests frequently
- [ ] Commit working states
- [ ] Keep changes focused
- [ ] Document non-obvious decisions

### After Refactoring
- [ ] Final test run (all tests pass)
- [ ] Code review
- [ ] Update documentation
- [ ] Verify no performance regression
- [ ] Clean commit history (squash if needed)

## Output Format
Provide refactoring plan in Japanese (translate all section titles and content to Japanese when presenting to user):

### Refactoring Target
[Code section to refactor with reasons]

### Goals
[What will be improved]

### Refactoring Plan
1. [Step 1 with expected outcome]
2. [Step 2 with expected outcome]
...

### Expected Results
**Improvements:**
- Readability: [explanation]
- Maintainability: [explanation]
- Performance: [explanation]

**Scope of Changes:**
- Modified files: [list]
- New files: [list]
- Test updates needed: [list]

### Risks and Mitigation
**Risks:** [potential issues]
**Mitigation:** [mitigation strategies]

### Commit Message
```
refactor: [description following project commit guidelines]

[detailed explanation]
```

## Anti-Patterns to Avoid
- ❌ Refactoring without tests
- ❌ Changing behavior during refactoring
- ❌ Making multiple unrelated changes
- ❌ Not running tests between changes
- ❌ Refactoring just for the sake of it
- ❌ Ignoring performance implications
