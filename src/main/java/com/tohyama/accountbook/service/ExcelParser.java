package com.tohyama.accountbook.service;

import com.tohyama.accountbook.dto.AccountDto;
import com.tohyama.accountbook.dto.SummaryDto;

import java.util.List;

public interface ExcelParser {
    List<?> extractAccountData(String excelFilePath, String mode) throws Exception;
    List<SummaryDto.DaillySummary> aggregateByDay(List<AccountDto> entries);
    List<SummaryDto.WeeklySummary> aggregateByWeek(List<AccountDto> entries);
    List<SummaryDto.MonthlySummary> aggregateByMonth(List<AccountDto> entries);
    List<AccountDto> filterCurrentWeek(List<AccountDto> entries);
    List<AccountDto> filterToday(List<AccountDto> entries);
    
    List<AccountDto> filterByYearMonth(List<AccountDto> entries, int year, int month);
    SummaryDto.MonthlySummary aggregateByYearMonth(List<AccountDto> entries, int year, int month);
}
