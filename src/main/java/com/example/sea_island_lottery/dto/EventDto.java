package com.example.sea_island_lottery.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class EventDto {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
    private Integer waitTime;
    private Integer queueNumber;
}
