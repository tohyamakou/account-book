package com.tohyama.accountbook.service;

public interface ReportGenerator {
    String generateDailyReport() throws Exception;
    String generateWeeklyReport() throws Exception;
    String generateMonthlyReport() throws Exception;
    
    String generateMonthlyReport(int year, int month, String modelName) throws Exception;
}
