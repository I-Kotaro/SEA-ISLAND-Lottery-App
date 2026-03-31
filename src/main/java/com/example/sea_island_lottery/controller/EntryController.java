package com.example.sea_island_lottery.controller;

import com.example.sea_island_lottery.entity.Entry;
import com.example.sea_island_lottery.entity.Event;
import com.example.sea_island_lottery.entity.User;
import com.example.sea_island_lottery.repository.EntryRepository;
import com.example.sea_island_lottery.service.EventService;
import com.example.sea_island_lottery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
public class EntryController {

    private final EventService eventService;
    private final EntryRepository entryRepository;
    private final UserService userService;

    @Autowired
    public EntryController(EventService eventService, EntryRepository entryRepository, UserService userService) {
        this.eventService = eventService;
        this.entryRepository = entryRepository;
        this.userService = userService;
    }

    @PostMapping("/entries/create")
    public String createEntry(@RequestParam("eventId") Long eventId, Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        User currentUser = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UUID currentUserId = currentUser.getId();

        Optional<Entry> existingEntry = entryRepository.findByUserIdAndEventId(currentUserId, eventId);

        Entry entry;
        if (existingEntry.isPresent()) {
            entry = existingEntry.get();
            if (!"WAITING".equals(entry.getStatus())) {
                entry.setStatus("WAITING");
                // 再応募（ステータスが更新された時）は、現在時刻を再設定して列の最後に並ぶようにする
                entry.setCreatedAt(LocalDateTime.now());
                entry = entryRepository.save(entry);
            }
        } else {
            Event event = new Event();
            event.setId(eventId);

            entry = new Entry();
            entry.setUser(currentUser);
            entry.setEvent(event);
            entry.setStatus("WAITING");
            entry = entryRepository.save(entry);
        }

        redirectAttributes.addFlashAttribute("showCompletionModal", true);
        return "redirect:/ticket";
    }

    // 受付完了orキャンセル時status指定
    @PostMapping("/entries/{id}/arrive")
    public String arrive(@PathVariable("id") Long id) {
        // 受付完了時はステータスをNOT_ENTEREDに変更し、ルートへ戻す
        eventService.updateEntryStatus(id, "NOT_ENTERED");
        return "redirect:/";
    }

    @PostMapping("/entries/{id}/cancel")
    public String cancel(@PathVariable("id") Long id) {
        // キャンセル時もステータスをNOT_ENTEREDに戻し、ルートへ戻す
        eventService.updateEntryStatus(id, "NOT_ENTERED");
        return "redirect:/";
    }
}
