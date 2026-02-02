package com.tohyama.accountbook.service;

import com.tohyama.accountbook.constants.consts;
import com.tohyama.accountbook.dto.AccountDto;
import com.tohyama.accountbook.dto.ItemCodeDto;
import com.tohyama.accountbook.dto.SummaryDto;
import com.tohyama.accountbook.utils.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExcelParserImpl implements ExcelParser {
    private static final Logger log = LoggerFactory.getLogger(ExcelParserImpl.class);

    public List<?> extractAccountData(String excelFilePath, String mode) throws Exception {
        return extractExcelData(excelFilePath, mode);
        
    }
    public List<SummaryDto.DaillySummary> aggregateByDay(List<AccountDto> entries) {
        Map<LocalDate, Integer> dailyMap = entries.stream()
                .collect(Collectors.groupingBy(AccountDto::getDate, Collectors.summingInt(AccountDto::getPrice)));
        List<SummaryDto.DaillySummary> result = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> e : dailyMap.entrySet()) {
            result.add(new SummaryDto.DaillySummary(e.getKey(), e.getValue()));
        }
        result.sort(Comparator.comparing(SummaryDto.DaillySummary::getDate));
        return result;
    }
    public List<SummaryDto.WeeklySummary> aggregateByWeek(List<AccountDto> entries) {
        Map<LocalDate, List<AccountDto>> weekMap = new HashMap<>();
        for (AccountDto entry : entries) {
            LocalDate date = entry.getDate();
            LocalDate weekStart = date.with(DayOfWeek.MONDAY);
            weekMap.computeIfAbsent(weekStart, k -> new ArrayList<>()).add(entry);
        }
        List<SummaryDto.WeeklySummary> result = new ArrayList<>();
        for (Map.Entry<LocalDate, List<AccountDto>> e : weekMap.entrySet()) {
            LocalDate weekStart = e.getKey();
            LocalDate weekEnd = weekStart.plusDays(6);
            int total = e.getValue().stream().mapToInt(AccountDto::getPrice).sum();
            result.add(new SummaryDto.WeeklySummary(weekStart, weekEnd, total));
        }
        result.sort(Comparator.comparing(SummaryDto.WeeklySummary::getWeekStart));
        return result;
    }
    public List<SummaryDto.MonthlySummary> aggregateByMonth(List<AccountDto> entries) {
        Map<String, Integer> monthMap = entries.stream()
                .collect(Collectors.groupingBy(e -> e.getDate().getYear() + "-" + e.getDate().getMonthValue(),
                        Collectors.summingInt(AccountDto::getPrice)));
        List<SummaryDto.MonthlySummary> result = new ArrayList<>();
        for (Map.Entry<String, Integer> e : monthMap.entrySet()) {
            String[] parts = e.getKey().split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            result.add(new SummaryDto.MonthlySummary(year, month, e.getValue()));
        }
        result.sort(Comparator.comparing(SummaryDto.MonthlySummary::getYear).thenComparing(SummaryDto.MonthlySummary::getMonth));
        return result;
    }
    /**
     * 月曜～本日まで
     */
    public List<AccountDto> filterCurrentWeek(List<AccountDto> entries) {
        LocalDate today = LocalDate.now();
        LocalDate monday = today.with(DayOfWeek.MONDAY);
        return entries.stream()
                .filter(e -> !e.getDate().isBefore(monday) && !e.getDate().isAfter(today))
                .collect(Collectors.toList());
    }
    public List<AccountDto> filterToday(List<AccountDto> entries) {
        LocalDate today = LocalDate.now();
        return entries.stream()
                .filter(e -> e.getDate().isEqual(today))
                .collect(Collectors.toList());
    }

    @Override
    public List<AccountDto> filterByYearMonth(List<AccountDto> entries, int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        
        return entries.stream()
                .filter(e -> !e.getDate().isBefore(startDate) && !e.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public SummaryDto.MonthlySummary aggregateByYearMonth(List<AccountDto> entries, int year, int month) {
        List<AccountDto> filtered = filterByYearMonth(entries, year, month);
        
        int totalAmount = filtered.stream()
                .mapToInt(AccountDto::getPrice)
                .sum();
        
        return new SummaryDto.MonthlySummary(year, month, totalAmount);
    }

    private List<?>  extractExcelData(String excelFilePath, String mode) throws Exception {
        List<AccountDto> allEntries = new ArrayList<>();
        try (InputStream is = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(is)) {
            log.info("ファイルロード成功");
            for (int idx = 0; idx < workbook.getNumberOfSheets(); idx++) {
                Sheet sheet = workbook.getSheetAt(idx);
                String sheetName = sheet.getSheetName();

                // item-codeは不要
                if(StringUtils.equals(consts.itemCodeSheetName, sheetName)) {
                    continue;
                }

                try {
                    List<AccountDto> entries = parseAccountEntries(workbook, sheetName);
                    if (CollectionUtils.isEmpty(entries)) {
                        log.warn("'{}'にデータがありません！", sheetName);
                        continue;
                    }
                    allEntries.addAll(entries);
                } catch (Exception e) {
                    log.error("エラー発生");
                    throw new Exception("データ抽出失敗", e);
                }
            }
        } catch (Exception e) {
            throw new Exception("ファイル読み込みに失敗しました。", e);
        }
        
        if ("day".equalsIgnoreCase(mode)) {
            return filterToday(allEntries);
        } else if ("week".equalsIgnoreCase(mode)) {
            return filterCurrentWeek(allEntries);
        } else if ("month".equalsIgnoreCase(mode)) {
            return allEntries;
        } else {
            return filterCurrentWeek(allEntries);
        }
    }
    /**
     * Item-Codeを抽出
     */
    private List<ItemCodeDto> extractItemCode(InputStream excelStream) throws Exception {
        List<ItemCodeDto> itemCodes = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(excelStream)) {
            Sheet sheet = workbook.getSheet("item-code");
            if (sheet == null) throw new IllegalArgumentException("item-codeシートがありません。");
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                String id = getStringCell(row, 0);
                String name = getStringCell(row, 1);
                String location = getStringCell(row, 2);
                int price = getIntCell(row, 3);
                if (StringUtils.isEmpty(id) || StringUtils.isEmpty(name) || StringUtils.isEmpty(location)) continue;
                itemCodes.add(new ItemCodeDto(id, name, location, price));
            }
        }
        return itemCodes;
    }
    /**
     * 月シートのデータを抽出
     */
    private List<AccountDto> parseAccountEntries(Workbook workbook, String monthSheet) throws Exception {
        List<AccountDto> entries = new ArrayList<>();
        Sheet sheet = workbook.getSheet(monthSheet);
        if (sheet == null) throw new IllegalArgumentException(monthSheet + "シートが存在しません。");
        DateTimeFormatter dashFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter slashFormatter = DateTimeFormatter.ofPattern("yyyy/M/d");
        for (int idx = 1; idx <= sheet.getLastRowNum(); idx++) {
            Row row = sheet.getRow(idx);
            if (row == null) continue;
            String dateStr = getStringCell(row, 0);
            String name = getStringCell(row, 1);
            String id = getStringCell(row, 2);
            int cnt = getIntCell(row, 3);
            int price = getIntCell(row, 4);
            String note = getStringCell(row, 5);

            if (dateStr == null || name == null || id == null) {
                continue;
            }
            LocalDate date;
            try {
                date = LocalDate.parse(dateStr, dashFormatter);
            } catch (Exception e1) {
                try {
                    date = LocalDate.parse(dateStr, slashFormatter);
                } catch (Exception e2) {
                    continue;
                }
            }
            entries.add(new AccountDto(date, name, id, cnt, price, note));
        }
        return entries;
    }

    /**
     * 文字列を抽出
     */
    private String getStringCell(Row row, int idx) {
        Cell cell = row.getCell(idx);
        if (cell == null) return null;

        CellType cellType = cell.getCellType();
        if (cellType == CellType.BLANK) return null;
        if (cellType == CellType.STRING) {
            String value = cell.getStringCellValue();
            return value.isEmpty() ? null : value;
        }
        if (cellType == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                LocalDate date = cell.getLocalDateTimeCellValue().toLocalDate();
                return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            return String.valueOf((int)cell.getNumericCellValue());
        }
        if (cellType == CellType.FORMULA) {
            CellType cachedType = cell.getCachedFormulaResultType();
            if (cachedType == CellType.STRING) {
                return cell.getStringCellValue();
            }
            if (cachedType == CellType.NUMERIC) {
                if (DateUtil.isCellDateFormatted(cell)) {
                    LocalDate date = cell.getLocalDateTimeCellValue().toLocalDate();
                    return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }
                return String.valueOf((int)cell.getNumericCellValue());
            }
        }
        return null;
    }
    /**
     * 数字を抽出
     */
    private int getIntCell(Row row, int idx) {
        Cell cell = row.getCell(idx);
        if (cell == null) return 0;

        CellType cellType = cell.getCellType();
        if (cellType == CellType.BLANK) return 0;
        if (cellType == CellType.NUMERIC) {
            return (int)cell.getNumericCellValue();
        }
        if (cellType == CellType.STRING) {
            try {
                return Integer.parseInt(cell.getStringCellValue());
            } catch (Exception e) {
                return 0;
            }
        }
        if (cellType == CellType.FORMULA) {
            CellType cachedType = cell.getCachedFormulaResultType();
            if (cachedType == CellType.NUMERIC) {
                return (int)cell.getNumericCellValue();
            }
            if (cachedType == CellType.STRING) {
                try { return Integer.parseInt(cell.getStringCellValue()); } catch (Exception e) { return 0; }
            }
        }
        return 0;
    }
}
