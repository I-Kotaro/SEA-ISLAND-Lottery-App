package com.example.sea_island_lottery.service;

import com.example.sea_island_lottery.dto.TicketDto;
import com.example.sea_island_lottery.entity.Entry;
import com.example.sea_island_lottery.repository.EntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TicketService {

    //ここで次呼ぶフォルダを EntryRepository, EventService 指定
    private final EntryRepository entryRepository;
    private final EventService eventService;

    @Autowired
    public TicketService(EntryRepository entryRepository, EventService eventService) {
        this.entryRepository = entryRepository;
        this.eventService = eventService;
    }

    public TicketDto getTicketInfo(Long entryId) {
        Optional<Entry> entryOptional = entryRepository.findById(entryId);
        if (entryOptional.isEmpty()) {
            return null;
        }

        //.get() => Optionalという箱から中身を取り出すデフォルトメソッド
        Entry entry = entryOptional.get();
        TicketDto ticketDto = new TicketDto();
        ticketDto.setEntryId(entry.getId());
        ticketDto.setUserName(entry.getUser().getName());
        ticketDto.setEventName(entry.getEvent().getName());

        long eventId = entry.getEvent().getId();

        // 自分より前に予約されている数（自分より早い作成日時かつWAITINGステータス）を取得
        long countBeforeMe = entryRepository.countByEventIdAndStatusAndCreatedAtBefore(
                eventId, "WAITING", entry.getCreatedAt());

        // イベントごとの1組あたりの平均時間を取得
        Integer avgTimePerQueue = entry.getEvent().getAvgTimePerQueue();
        int avg_time = (avgTimePerQueue != null) ? avgTimePerQueue : 0;

        // 待ち時間 = 自分より前の組数 × 各イベント時間
        int displayWaitTime = (int) (countBeforeMe * avg_time);

        ticketDto.setQueueNumber((int) countBeforeMe);
        ticketDto.setWaitTime(displayWaitTime);

        return ticketDto;
    }

    //指定されたユーザーの最新応募中チケットを取得
    public TicketDto getLatestWaitingTicketByUserId(java.util.UUID userId) {
        return entryRepository.findByUserIdAndStatus(userId, "WAITING").stream()
                .findFirst()
                .map(entry -> getTicketInfo(entry.getId()))
                .orElse(null);
    }
}
