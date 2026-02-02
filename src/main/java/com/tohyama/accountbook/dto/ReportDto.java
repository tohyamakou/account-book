package com.tohyama.accountbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {
    private LocalDateTime createdAt;
    private String type;
    private String content;
    private Integer year;
    private Integer month;
    private String modelUsed;
}
