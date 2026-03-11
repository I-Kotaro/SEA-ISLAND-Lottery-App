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

import java.util.UUID;

@Controller
public class EntryController {

    private final EventService eventService;
    private final EntryRepository entryRepository;

    @Autowired
    public EntryController(EventService eventService, EntryRepository entryRepository) {
        this.eventService = eventService;
        this.entryRepository = entryRepository;
    }

    @PostMapping("/entries/create")
    public String createEntry(@RequestParam Long eventId, @RequestParam UUID userId) {
        // 擬似的なリポジトリやサービスを介してエンティティを取得
        User user = new User();
        user.setId(userId);

        Event event = new Event();
        event.setId(eventId);

        // 既存の応募がないか確認
        if (entryRepository.findByUserIdAndEventId(userId, eventId).isEmpty()) {
            Entry entry = new Entry();
            entry.setUser(user);
            entry.setEvent(event);
            entry.setStatus("WAITING");
            entryRepository.save(entry);
        }

        return "redirect:/events/" + eventId;
    }

    @PostMapping("/entries/{id}/arrive")
    public String arrive(@PathVariable Long id, @RequestParam Long eventId) {
        eventService.updateEntryStatus(id, "NOT_ENTERED");
        return "redirect:/events/" + eventId;
    }
}
