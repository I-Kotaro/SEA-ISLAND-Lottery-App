package com.example.sea_island_lottery.controller;

import com.example.sea_island_lottery.entity.Entry;
import com.example.sea_island_lottery.entity.Event;
import com.example.sea_island_lottery.entity.User;
import com.example.sea_island_lottery.repository.EntryRepository;
import com.example.sea_island_lottery.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.UUID;

@Controller
public class EntryController {

    private final EventService eventService;
    private final EntryRepository entryRepository;

    // テスト用の固定ユーザーID
    private final UUID TEST_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Autowired
    public EntryController(EventService eventService, EntryRepository entryRepository) {
        this.eventService = eventService;
        this.entryRepository = entryRepository;
    }

    @PostMapping("/entries/create")
    public String createEntry(@RequestParam("eventId") Long eventId) {
        // 固定のテストユーザーを使用
        Optional<Entry> existingEntry = entryRepository.findByUserIdAndEventId(TEST_USER_ID, eventId);

        Entry entry;
        if (existingEntry.isPresent()) {
            entry = existingEntry.get();
            if (!"WAITING".equals(entry.getStatus())) {
                entry.setStatus("WAITING");
                entry = entryRepository.save(entry);
            }
        } else {
            User user = new User();
            user.setId(TEST_USER_ID);

            Event event = new Event();
            event.setId(eventId);

            entry = new Entry();
            entry.setUser(user);
            entry.setEvent(event);
            entry.setStatus("WAITING");
            entry = entryRepository.save(entry);
        }

        // 完了パラメータを付けてイベント詳細へリダイレクト
        return "redirect:/events/" + eventId + "?completed=true";
    }

    @PostMapping("/entries/{id}/arrive")
    public String arrive(@PathVariable("id") Long id) {
        Optional<Entry> entryOptional = entryRepository.findById(id);
        if (entryOptional.isEmpty()) {
            return "redirect:/";
        }
        Entry entry = entryOptional.get();
        eventService.updateEntryStatus(id, "NOT_ENTERED");
        return "redirect:/events/" + entry.getEvent().getId();
    }

    @PostMapping("/entries/{id}/cancel")
    public String cancel(@PathVariable("id") Long id) {
        Optional<Entry> entryOptional = entryRepository.findById(id);
        if (entryOptional.isEmpty()) {
            return "redirect:/";
        }
        Entry entry = entryOptional.get();
        eventService.updateEntryStatus(id, "NOT_ENTERED");
        return "redirect:/events/" + entry.getEvent().getId();
    }
}
