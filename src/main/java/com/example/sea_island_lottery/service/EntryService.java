package com.example.sea_island_lottery.service;

import com.example.sea_island_lottery.dto.EntryDto;
import com.example.sea_island_lottery.entity.Entry;
import com.example.sea_island_lottery.entity.Event;
import com.example.sea_island_lottery.entity.User;
import com.example.sea_island_lottery.repository.EntryRepository;
import com.example.sea_island_lottery.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EntryService {

    private final EntryRepository entryRepository;
    private final EventRepository eventRepository;

    @Autowired
    public EntryService(EntryRepository entryRepository, EventRepository eventRepository) {
        this.entryRepository = entryRepository;
        this.eventRepository = eventRepository;
    }

    //DTO使用パターンで作成が必要なメソッド＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
    // DTOを返すメソッド
    @Transactional(readOnly = true)
    public List<EntryDto> findEntryDtoByUserId(User user) {
        return entryRepository.findByUserId(user.getId()).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // EntryエンティティをEntryDtoに変換するヘルパーメソッド
    private EntryDto convertToDto(Entry entry) {
        EntryDto dto = new EntryDto();
        dto.setId(entry.getId());
        dto.setEventId(entry.getEvent().getId());
        dto.setEventName(entry.getEvent().getName());
        // entryDate は Entry エンティティから削除されたため、設定しない
        dto.setStatus(entry.getStatus());
        dto.setCreatedAt(entry.getCreatedAt());
        return dto;
    }
    //＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝

    public Entry createEntry(User user, Long eventId) {
        // 1. イベントが存在するか確認
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

        // 2. 応募が締め切られていないか確認
        // if (LocalDateTime.now().isAfter(event.getEntryDeadline())) {
        //     throw new RuntimeException("Entry for this event is already closed.");
        // }

        // 4. 既に応募済みでないか確認 (これはDBのUNIQUE制約でも保証されるが、アプリケーションレベルでもチェック)
        // entryRepository.findByUserIdAndEventId(user.getId(), eventId).ifPresent(e -> {
        //     throw new RuntimeException("User has already entered this event.");
        // });

        // 5. 応募データを作成
        Entry newEntry = new Entry();
        newEntry.setUser(user);
        newEntry.setEvent(event);
        newEntry.setStatus("WAITING"); // 初期ステータスは 'WAITING'
        // entryDate は Entry エンティティから削除されたため、設定しない

        return entryRepository.save(newEntry);
    }

    @Transactional(readOnly = true)
    public List<Entry> findEntriesByUserId(User user) {
        return entryRepository.findByUserId(user.getId());
    }

}
