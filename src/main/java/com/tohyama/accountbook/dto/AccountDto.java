package com.tohyama.accountbook.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AccountDto {
    private LocalDate date;
    private String name;
    private String id;
    private int cnt;
    private int price;
    private String note;

    public AccountDto(LocalDate date, String name, String id, int cnt, int price, String note) {
        this.date = date;
        this.name = name;
        this.id = id;
        this.cnt = cnt;
        this.price = price;
        this.note = note;
    }
}