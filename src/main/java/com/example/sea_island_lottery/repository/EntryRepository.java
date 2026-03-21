//どのテーブルからどのカラムを、どんな条件で取得するかを掛けあわせて抽出する層
//エンティティ層とビジネスロジック層でどういうカラムを使うか橋渡しする層でもある
package com.example.sea_island_lottery.repository;

import com.example.sea_island_lottery.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {

    // ユーザーの応募をすべて取得
    List<Entry> findByUserId(UUID userId);

    // イベントIDとステータスが一致する応募をカウント
    long countByEventIdAndStatus(Long eventId, String status);

    // ステータスと作成日時を使って3時間前の応募を取得
    List<Entry> findByStatusAndCreatedAtBefore(String status, LocalDateTime createdAt);

    // ユーザーIDとイベントIDで応募中イベントを取得
    Optional<Entry> findByUserIdAndEventId(UUID userId, Long eventId);

    List<Entry> findByUserIdAndStatus(UUID userId, String status);

    // ユーザーIDとステータスで応募が存在するかチェック
    boolean existsByUserIdAndStatus(UUID userId, String status);

    // ユーザーIDとステータスで、応募中イベント以外で他にWAITUNGステータスイベントが存在するかチェック
    boolean existsByUserIdAndStatusAndEventIdNot(UUID userId, String status, Long eventId);

    // アカウント削除時にユーザーIDと紐づく応募情報をすべて削除
    //  DELETE FROM entries WHERE user_id = ? が生成される
    void deleteByUserId(UUID userId);

}
