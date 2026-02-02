package com.tohyama.accountbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class SummaryDto {
    
    @Data
    @AllArgsConstructor
    public static class DaillySummary {
        private LocalDate date;
        private int totalAmount;
    }
    
    @Data
    @AllArgsConstructor
    public static class MonthlySummary {
        private int year;
        private int month;
        private int totalAmount;
    }
    
    @Data
    @AllArgsConstructor
    public static class WeeklySummary {
        private LocalDate weekStart;
        private LocalDate weekEnd;
        private int totalAmount;
    }
}
