package com.example.sea_island_lottery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    private String title;
    private String description;
    private String imageUrl;
    private String buttonText;
    private String buttonLink; // ボタンのリンク先も追加
}
