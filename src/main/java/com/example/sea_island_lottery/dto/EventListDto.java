package com.example.sea_island_lottery.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EventListDto {
    private Long id;
    private String name;
    private String imageUrl;
    private LocalDate eventDate;
    private String status;
}
