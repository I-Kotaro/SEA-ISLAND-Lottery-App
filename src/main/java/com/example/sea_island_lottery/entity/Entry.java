package com.example.sea_island_lottery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "entries", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "event_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entry {

    //一意に識別
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //どのユーザーが応募したかを記録
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //どのイベントに応募してるか記録
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    //未応募NOT_ENTERED, 応募中WAITING, 完了COMPLETEDを状態管理
    @Column(nullable = false)
    private String status;

    //応募日時を正確に記録, 3時間を判断する基準点
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
