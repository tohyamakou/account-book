package com.tohyama.accountbook.service;

import com.tohyama.accountbook.dto.GeminiDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportGeneratorImpl implements ReportGenerator {
    private static final Logger log = LoggerFactory.getLogger(ReportGeneratorImpl.class);

    @Value("${ai.excel.path}")
    private String excelFilePath;
    
    private final ExcelParser excelParser;
    private final GeminiProxy geminiProxy;

    public ReportGeneratorImpl(ExcelParser excelParser, GeminiProxy geminiProxy) {
        this.excelParser = excelParser;
        this.geminiProxy = geminiProxy;
    }

    @Override
    public String generateDailyReport() throws Exception {
        List<?> dailyData = getDailyReportData(excelFilePath);
        GeminiDto.GeminiProxyRequest request = new GeminiDto.GeminiProxyRequest("daily", dailyData);
        GeminiDto.GeminiProxyResponse response = geminiProxy.analyze(request);
        return response.getReport();
    }

    @Override
    public String generateWeeklyReport() throws Exception {
        List<?> weeklyData = getWeeklyReportData(excelFilePath);
        GeminiDto.GeminiProxyRequest request = new GeminiDto.GeminiProxyRequest("weekly", weeklyData);
        GeminiDto.GeminiProxyResponse response = geminiProxy.analyze(request);
        return response.getReport();
    }

    @Override
    public String generateMonthlyReport() throws Exception {
        java.time.LocalDate now = java.time.LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        
        @SuppressWarnings("unchecked")
        List<com.tohyama.accountbook.dto.AccountDto> allData = 
            (List<com.tohyama.accountbook.dto.AccountDto>) excelParser.extractAccountData(excelFilePath, "month");
        
        com.tohyama.accountbook.dto.SummaryDto.MonthlySummary summary = 
            excelParser.aggregateByYearMonth(allData, year, month);
        
        GeminiDto.GeminiProxyRequest request = new GeminiDto.GeminiProxyRequest("monthly", summary);
        GeminiDto.GeminiProxyResponse response = geminiProxy.analyze(request);
        
        return response.getReport();
    }

    @Override
    @SuppressWarnings("unchecked")
    public String generateMonthlyReport(int year, int month, String modelName) throws Exception {
        List<?> allData = excelParser.extractAccountData(excelFilePath, "month");
        
        com.tohyama.accountbook.dto.SummaryDto.MonthlySummary summary = 
            excelParser.aggregateByYearMonth((java.util.List<com.tohyama.accountbook.dto.AccountDto>) allData, year, month);
        
        GeminiDto.GeminiProxyRequest request = new GeminiDto.GeminiProxyRequest("monthly", summary, modelName);
        GeminiDto.GeminiProxyResponse response = geminiProxy.analyze(request);
        
        return response.getReport();
    }

    private List<?> getDailyReportData(String excelFilePath) throws Exception {
        return excelParser.extractAccountData(excelFilePath, "day");
    }
    
    private List<?> getWeeklyReportData(String excelFilePath) throws Exception {
        return excelParser.extractAccountData(excelFilePath, "week");
    }
}
