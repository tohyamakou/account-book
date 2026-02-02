package com.tohyama.accountbook.controller;

import com.tohyama.accountbook.service.ReportFileService;
import com.tohyama.accountbook.service.ReportFileServiceImpl;
import com.tohyama.accountbook.service.ReportGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private static final Logger log = LoggerFactory.getLogger(ReportController.class);
    
    private final ReportGenerator reportGenerator;
    private final ReportFileService reportFileService;

    @Value("${report.save.directory:C:/Temp/reports}")
    private String defaultSaveDirectory;

    public ReportController(ReportGenerator reportGenerator, ReportFileService reportFileService) {
        this.reportGenerator = reportGenerator;
        this.reportFileService = reportFileService;
    }

    @PostMapping("/daily")
    public ResponseEntity<Map<String, String>> saveDailyReport(
            @RequestParam(required = false) String directory) {
        try {
            String report = reportGenerator.generateDailyReport();
            String saveDir = (directory != null && !directory.trim().isEmpty()) ? directory : defaultSaveDirectory;
            String fileName = generateDailyFileName();
            String filePath = reportFileService.saveReportToFile(report, fileName, saveDir);
            
            Map<String, String> response = new HashMap<>();
            response.put("filePath", filePath);
            response.put("message", "日別分析を保存しました。");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("日別リポート保存失敗: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "エラー発生: " + e.getMessage()));
        }
    }
    
    @PostMapping("/weekly")
    public ResponseEntity<Map<String, String>> saveWeeklyReport(
            @RequestParam(required = false) String directory) {
        try {
            String report = reportGenerator.generateWeeklyReport();
            String saveDir = (directory != null && !directory.trim().isEmpty()) ? directory : defaultSaveDirectory;
            String fileName = generateWeeklyFileName();
            String filePath = reportFileService.saveReportToFile(report, fileName, saveDir);
            
            Map<String, String> response = new HashMap<>();
            response.put("filePath", filePath);
            response.put("message", "週別分析を保存しました。");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("週別リポート保存失敗: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "エラー発生: " + e.getMessage()));
        }
    }
    
    @PostMapping("/monthly")
    public ResponseEntity<Map<String, String>> saveMonthlyReport(
            @RequestParam(required = false) String directory) {
        try {
            String report = reportGenerator.generateMonthlyReport();
            String saveDir = (directory != null && !directory.trim().isEmpty()) ? directory : defaultSaveDirectory;
            
            LocalDate now = LocalDate.now();
            String fileName = ReportFileServiceImpl.generateFileName(now.getYear(), now.getMonthValue());
            String filePath = reportFileService.saveReportToFile(report, fileName, saveDir);
            
            Map<String, String> response = new HashMap<>();
            response.put("filePath", filePath);
            response.put("message", "月別分析を保存しました。");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("月別リポート保存失敗: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "エラー発生: " + e.getMessage()));
        }
    }
    @PostMapping("/monthly/save")
    public ResponseEntity<Map<String, String>> saveCustomMonthlyReport(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(required = false) String modelName,
            @RequestParam(required = false) String directory) {
        try {
            String report = reportGenerator.generateMonthlyReport(year, month, modelName);
            String saveDir = (directory != null && !directory.trim().isEmpty()) ? directory : defaultSaveDirectory;
            String fileName = ReportFileServiceImpl.generateFileName(year, month);
            String filePath = reportFileService.saveReportToFile(report, fileName, saveDir);
            
            Map<String, String> response = new HashMap<>();
            response.put("filePath", filePath);
            response.put("message", "ファイルを保存しました。");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("月別リポート保存失敗: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "エラー発生: " + e.getMessage()));
        }
    }

    private String generateDailyFileName() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        return String.format("daily-report-%s.md", now.format(dateFormatter));
    }
    private String generateWeeklyFileName() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        return String.format("weekly-report-%s.md", now.format(dateFormatter));
    }
}
