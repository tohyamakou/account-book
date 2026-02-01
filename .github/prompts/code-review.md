# Code Review Prompt

## Purpose
Guide AI to perform comprehensive code review focusing on quality, security, and best practices.

## Instructions
When reviewing code, analyze the following aspects:

### 1. Code Quality
- [ ] Readability: Is the code easy to understand?
- [ ] Naming: Are variables, methods, and classes well-named?
- [ ] Structure: Is the code well-organized?
- [ ] Complexity: Are there overly complex methods that should be simplified?
- [ ] DRY principle: Is there duplicated code?

### 2. Best Practices
- [ ] Design patterns: Are appropriate patterns used?
- [ ] SOLID principles: Are they followed?
- [ ] Error handling: Are exceptions handled properly?
- [ ] Logging: Is logging appropriate and meaningful?
- [ ] Comments: Are complex parts well-documented?

### 3. Performance
- [ ] Efficiency: Are there performance bottlenecks?
- [ ] Resource management: Are resources properly closed?
- [ ] Memory usage: Are there potential memory leaks?
- [ ] Database queries: Are they optimized? (N+1 problem?)

### 4. Security
- [ ] Input validation: Is user input properly validated?
- [ ] SQL injection: Are prepared statements used?
- [ ] XSS prevention: Is output properly escaped?
- [ ] Authentication/Authorization: Are they properly implemented?
- [ ] Sensitive data: Are credentials/secrets properly handled?

### 5. Testing
- [ ] Test coverage: Are critical paths tested?
- [ ] Edge cases: Are edge cases covered?
- [ ] Test quality: Are tests meaningful and maintainable?

### 6. Project-Specific
- [ ] Framework conventions: Are they followed?
- [ ] Project coding standards: Are instruction files followed?
- [ ] Testing guidelines: Are testing standards met?
- [ ] Git commits: Are commit message guidelines followed?

## Output Format
Provide review in Japanese (translate all section titles and content to Japanese when presenting to user) with the following structure:

### Positive Aspects
[List what was done well]

### Areas for Improvement
[List issues found with severity level]

#### 🔴 Critical
- Issue 1: [Description and suggestion]

#### 🟡 Warning
- Issue 1: [Description and suggestion]

#### 🟢 Suggestion
- Issue 1: [Description and suggestion]

### Code Suggestions
```java
// Improved code example
```

### Additional Notes
[Additional notes, links to relevant documentation]
