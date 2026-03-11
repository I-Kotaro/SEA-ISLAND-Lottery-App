package com.example.sea_island_lottery.service;

import com.example.sea_island_lottery.dto.EventDto;
import com.example.sea_island_lottery.entity.Entry;
import com.example.sea_island_lottery.entity.Event;
import com.example.sea_island_lottery.repository.EntryRepository;
import com.example.sea_island_lottery.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventService {

    //DI注入準備
    private final EventRepository eventRepository;
    private final EntryRepository entryRepository; // EntryRepository を追加

    //自動でDI注入(new)するアノテーション
    @Autowired
    public EventService(EventRepository eventRepository, EntryRepository entryRepository) { // コンストラクタを修正
        this.eventRepository = eventRepository;
        this.entryRepository = entryRepository; // 初期化
    }

    // DTOを返す新しいメソッド
    @Transactional(readOnly = true)
    public List<EventDto> findAllEventsForList() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // EventエンティティをEventListDtoに変換するヘルパーメソッド  DTO使用で必須
    public EventDto convertToDto(Event event) {
        EventDto dto = new EventDto();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setDescription(event.getDescription()); // description をセット
        dto.setImageUrl(event.getImageUrl());
        dto.setStartTime(event.getStartTime());
        dto.setEndTime(event.getEndTime());
        dto.setStatus(event.getStatus());

        // 待ち組数を取得
        long queueNumber = entryRepository.countByEventIdAndStatus(event.getId(), "WAITING");
        dto.setQueueNumber((int) queueNumber);

        // 待ち時間を計算
        Integer avgTimePerQueue = event.getAvgTimePerQueue();
        if (avgTimePerQueue != null && avgTimePerQueue > 0) {
            dto.setWaitTime((int) queueNumber * avgTimePerQueue);
        } else {
            dto.setWaitTime(0); // 平均時間が設定されていない場合は0分
        }

        return dto;
    }

    //イベントを全件取得するメソッド
    //DB操作がトランザクション内(処理成功時だけDBに反映される)で実行されるアноテーション
    @Transactional(readOnly = true)
    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    //イベントを1件取得するメソッド
    @Transactional(readOnly = true)
    public Optional<Event> findEventById(Long id) {
        return eventRepository.findById(id);
    }

    // 応募ステータスを更新するメソッド
    public void updateEntryStatus(Long entryId, String status) {
        Entry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry not found with id: " + entryId));
        entry.setStatus(status);
        entryRepository.save(entry);
    }

    // 3時間以上 "WAITING" ステータスの応募をリセットするスケジュールされたタスク
    @Scheduled(fixedRate = 3600000) // 1時間ごとに実行
    public void resetOldWaitingEntries() {
        LocalDateTime threeHoursAgo = LocalDateTime.now().minusHours(3);
        List<Entry> oldEntries = entryRepository.findByStatusAndCreatedAtBefore("WAITING", threeHoursAgo);
        for (Entry entry : oldEntries) {
            entry.setStatus("NOT_ENTERED");
            entryRepository.save(entry);
        }
    }
}
