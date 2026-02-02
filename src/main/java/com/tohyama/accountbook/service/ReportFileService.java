package com.tohyama.accountbook.service;

public interface ReportFileService {
  
    String saveReportToFile(String content, String fileName, String directory) throws Exception;
}
