package com.example.sea_island_lottery.service;

import com.example.sea_island_lottery.dto.TicketDto;
import com.example.sea_island_lottery.entity.Entry;
import com.example.sea_island_lottery.repository.EntryRepository;
import com.example.sea_island_lottery.repository.EventRepository;
import com.example.sea_island_lottery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TicketService {

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

        ticketDto.setQueueNumber(queueNumber);
        ticketDto.setWaitTime(waitTime);


        return ticketDto;
    }
}
