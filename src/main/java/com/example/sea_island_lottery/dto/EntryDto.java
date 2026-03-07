package com.example.sea_island_lottery.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class EntryDto {
    private Long id;
    private Long eventId;
    private String eventName;
    private LocalDate entryDate;
    private String status;
    private LocalDateTime createdAt;
}
