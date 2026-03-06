package com.example.sea_island_lottery.repository;

import com.example.sea_island_lottery.entity.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {

    // 例: 特定のユーザーの応募をすべて検索するメソッド
    List<Entry> findByUserId(UUID userId);

    // 例: 特定のイベントへの応募をすべて検索するメソッド
    List<Entry> findByEventId(Long eventId);

}
