package com.example.sea_island_lottery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    //ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //タイトル：入力必須
    @Column(nullable = false)
    private String name;

    //説明：入力必須
    @Column(columnDefinition = "TEXT")
    private String description;

    // 画像URL
    @Column(name = "image_url")
    private String imageUrl;

    //定員：入力必須
    @Column(nullable = false)
    private Integer capacity;

    //応募締切：入力必須
    @Column(name = "entry_deadline", nullable = false)
    private LocalDateTime entryDeadline;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
