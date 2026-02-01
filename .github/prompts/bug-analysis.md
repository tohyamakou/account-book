# Bug Analysis Prompt

## Purpose
Guide AI to systematically analyze and debug issues in the codebase.

## Instructions
When analyzing a bug or error, follow these steps:

### 1. Problem Identification
- [ ] What is the expected behavior?
- [ ] What is the actual behavior?
- [ ] When does the issue occur? (always, intermittently, specific conditions)
- [ ] What is the error message or symptom?
- [ ] Stack trace analysis (if available)

### 2. Context Gathering
- [ ] Which component/module is affected?
- [ ] What was the last working version?
- [ ] Recent changes that might be related
- [ ] User actions that trigger the issue
- [ ] Environment (dev, test, prod)

### 3. Root Cause Analysis
- [ ] Trace the execution flow
- [ ] Identify the exact point of failure
- [ ] Analyze variable states at failure point
- [ ] Check for null pointer, type mismatch, etc.
- [ ] Review related code and dependencies
- [ ] Check logs for additional clues

### 4. Reproduction Steps
- [ ] Can the bug be reproduced consistently?
- [ ] What are the minimal steps to reproduce?
- [ ] Required test data or environment setup
- [ ] Create a failing test case

### 5. Impact Assessment
- [ ] How many users are affected?
- [ ] What functionality is broken?
- [ ] Are there workarounds available?
- [ ] Is data integrity at risk?
- [ ] Priority level (Critical, High, Medium, Low)

### 6. Solution Proposal
- [ ] Root cause explanation
- [ ] Proposed fix approach
- [ ] Alternative solutions (if any)
- [ ] Potential side effects
- [ ] Testing strategy for the fix

### 7. Prevention
- [ ] Why wasn't this caught earlier?
- [ ] What tests should be added?
- [ ] Are there similar issues elsewhere?
- [ ] Process improvements needed?

## Common Bug Categories

### Data Processing Bugs
- Date parsing errors (timezone issues)
- Data type mismatches
- Null/empty value handling
- Complex data structure evaluation
- Memory issues with large datasets
- Character encoding problems

### Framework-Specific Bugs
- Bean initialization failures
- Circular dependencies
- Transaction management issues
- Database connection leaks
- File upload size limits
- Configuration errors

### Logic Bugs
- Off-by-one errors
- Incorrect null checks
- Missing edge case handling
- Wrong assumptions about data
- Race conditions

## Output Format
Provide analysis in Japanese (translate all section titles and content to Japanese when presenting to user):

### Problem Summary
[Concise problem description]

### Root Cause
[Root cause explanation with code references]

### Reproduction Steps
```
Step 1: ...
Step 2: ...
Expected: ...
Actual: ...
```

### Solution
[Proposed fix with code example]

```java
// Before (problem code)

// After (fixed code)
```

### Test Plan
- [ ] Test case 1
- [ ] Test case 2

### Prevention
[How to prevent similar issues]

## Debugging Tools
- Use breakpoints in IDE
- Enable debug logging
- Use framework-specific monitoring tools for metrics
- Check database query logs
- Monitor memory usage
- Review recent git commits: `git log --oneline -n 10`
