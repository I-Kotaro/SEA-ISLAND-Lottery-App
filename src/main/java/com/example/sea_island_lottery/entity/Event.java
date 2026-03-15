package com.example.sea_island_lottery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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

    //開始時間
    @Column(name = "start_time")
    private LocalTime startTime;

    //終了時間
    @Column(name = "end_time")
    private LocalTime endTime;

    //ステータス：入力必須
    @Column(nullable = false)
    private String status; // 未応募NOT_ENTERED, 応募中WAITING, 完了COMPLETED

    // 1組あたりの平均時間（分）
    @Column(name = "avg_time_per_queue")
    private Integer avgTimePerQueue;

    //    @Data機能でLombokによって自動生成されるコード avgTimePerQueueパターン
    //    // ... フィールド定義 ...
    //    private Integer avgTimePerQueue; => これをEventServiceで使用
    //
    //    // ゲッター (Getter)
    //    public Integer getAvgTimePerQueue() {
    //        return this.avgTimePerQueue;
    //    }
    //
    //    // セッター (Setter)
    //    public void setAvgTimePerQueue(Integer avgTimePerQueue) {
    //        this.avgTimePerQueue = avgTimePerQueue;
    //    }
    //
    //    // `equals`, `hashCode`, `toString` も自動生成される
    //    @Override
    //    public boolean equals(Object o) { ... }
    //
    //    @Override
    //    public int hashCode() { ... }
    //
    //    @Override
    //    public String toString() {
    //        return "Event(id=" + this.id + ", name=" + this.name + ", ... avgTimePerQueue=" + this.avgTimePerQueue + ")";
    //    }

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
