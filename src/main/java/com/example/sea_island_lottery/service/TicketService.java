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

        Entry entry = entryOptional.get();
        TicketDto ticketDto = new TicketDto();
        ticketDto.setEntryId(entry.getId());
        ticketDto.setUserName(entry.getUser().getName());
        ticketDto.setEventName(entry.getEvent().getName());

        long eventId = entry.getEvent().getId();
        int queueNumber = eventService.getQueueNumber(eventId);
        int waitTime = eventService.getWaitTime(eventId);

        // 待機組数から1を引く (ただし0未満にはならないようにする)
        int displayQueueNumber = Math.max(0, queueNumber - 1);
        int displayWaitTime = Math.max(0, waitTime - 900);

        ticketDto.setQueueNumber(displayQueueNumber);
        ticketDto.setWaitTime(displayWaitTime);

        return ticketDto;
    }
}
