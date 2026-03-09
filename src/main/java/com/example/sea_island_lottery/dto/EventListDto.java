package com.example.sea_island_lottery.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class EventListDto {
    private Long id;
    private String name;
    private String imageUrl;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
}
