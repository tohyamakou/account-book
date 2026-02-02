package com.tohyama.accountbook.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ReportFileServiceImpl implements ReportFileService {

    @Override
    public String saveReportToFile(String content, String fileName, String directory) throws Exception {
        try {
            Path dirPath = Paths.get(directory);
            
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            
            Path filePath = dirPath.resolve(fileName);
            
            Files.writeString(filePath, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            
            return filePath.toString();
            
        } catch (IOException e) {
            throw new Exception(e.getMessage(), e);
        }
    }
    
    public static String generateFileName(int year, int month) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        return String.format("monthly-report-%04d-%02d-%s.md", year, month, now.format(dateFormatter));
    }
}
