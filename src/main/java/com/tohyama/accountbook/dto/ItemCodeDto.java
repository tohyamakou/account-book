package com.tohyama.accountbook.dto;

import lombok.Data;

@Data
public class ItemCodeDto {
    private String id;
    private String name;
    private String location;
    private int price;
    
    public ItemCodeDto(String id, String name, String location, int price) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.price = price;
    }
}
