# Excel File Processing Instructions

## Purpose
Guidelines for handling Excel file uploads, parsing, and data processing in this project.

## Libraries and Tools

### Apache POI
- Use Apache POI for Excel file processing
- WorkbookFactory for auto-detecting file format (.xls, .xlsx)
- Use streaming API (SXSSF) for large files to reduce memory usage
- Close workbooks and streams properly to prevent memory leaks

## File Upload Flow

### Controller Layer
- Accept MultipartFile parameter
- Validate file extension and MIME type
- Check file size before processing
- Return meaningful error messages for invalid files
- Use @PostMapping with consumes = MediaType.MULTIPART_FORM_DATA_VALUE

### Service Layer
- Separate file validation from parsing logic
- Use try-with-resources for workbook handling
- Process files in streaming mode when possible
- Return structured result objects (success/error status, parsed data, error messages)

## Excel Parsing Best Practices

### Reading Data
- Skip empty rows
- Handle missing cells gracefully (null checks)
- Validate header row if format is fixed
- Support flexible column ordering when possible
- Read cell values based on cell type (NUMERIC, STRING, BOOLEAN, FORMULA, etc.)

### Data Type Conversion
- Use DataFormatter for consistent string conversion
- Handle date cells properly (DateUtil.isCellDateFormatted)
- Convert numeric cells carefully (avoid precision loss)
- Trim string values to remove leading/trailing spaces
- Handle formula cells by evaluating or getting cached values

### Error Handling
- Catch specific exceptions (InvalidFormatException, IOException, etc.)
- Provide row and column information in error messages
- Continue processing when possible (skip invalid rows)
- Collect all errors and return summary
- Log parsing errors with file name and timestamp

### Validation
- Validate required columns exist
- Check data types match expected format
- Validate business rules (date ranges, numeric ranges, etc.)
- Verify data integrity (foreign key references, duplicates, etc.)
- Provide detailed validation error messages with row numbers

## Performance Optimization

### Memory Management
- Use streaming API for files >10MB
- Process in chunks/batches
- Clear data structures after processing
- Set appropriate JVM heap size for large file processing

### Processing Speed
- Avoid unnecessary cell reads
- Cache frequently accessed data
- Use bulk inserts for database operations
- Process files asynchronously for large uploads

## Testing

### Unit Tests
- Test with various Excel formats (.xls, .xlsx)
- Test edge cases: empty file, single row, large file
- Test error conditions: corrupted file, wrong format, missing columns
- Mock file upload in tests
- Use sample Excel files in test resources

### Sample Files
- Create diverse sample files for testing
- Include valid and invalid data samples
- Test boundary conditions
- Test special characters and Unicode
- Test merged cells and formatted cells

## Configuration

### Application Settings
```yaml
spring:
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB
      enabled: true
```

### Error Messages
- Return error messages in Japanese for users
- Include row number and column name in validation errors
- Provide actionable suggestions for fixing errors
- Format: 「[行番号][列名] の形式が不正です。[期待される形式] を使用してください。」

## Common Issues and Solutions

### Issue: OutOfMemoryError with large files
- Solution: Use SXSSF WorkbookFactory and streaming API

### Issue: Date values read as numbers
- Solution: Use DateUtil.isCellDateFormatted() and proper date conversion

### Issue: Formula cells returning null
- Solution: Use evaluator.evaluate() or cell.getCachedFormulaResultType()

### Issue: Merged cells causing data loss
- Solution: Detect merged regions and handle appropriately

### Issue: Inconsistent data types
- Solution: Validate cell type before reading, provide clear error messages