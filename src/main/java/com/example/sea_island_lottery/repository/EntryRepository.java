//どのテーブルからどのカラムを、どんな条件で取得するかを掛けあわせて抽出する層
//エンティティ層とビジネスロジック層でどういうカラムを使うか橋渡しする層でもある
package com.example.sea_island_lottery.repository;

import com.example.sea_island_lottery.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {

    // ユーザーの応募をすべて検索
    List<Entry> findByUserId(UUID userId);

    // イベントIDとステータスが一致する応募をカウント
    long countByEventIdAndStatus(Long eventId, String status);

    // ステータスと作成日時で応募を検索
    List<Entry> findByStatusAndCreatedAtBefore(String status, LocalDateTime createdAt);

}
