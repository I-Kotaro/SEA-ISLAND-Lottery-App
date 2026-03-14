package com.example.sea_island_lottery.controller;

import com.example.sea_island_lottery.dto.EventDto;
import com.example.sea_island_lottery.entity.Entry;
import com.example.sea_island_lottery.entity.Event;
import com.example.sea_island_lottery.repository.EntryRepository;
import com.example.sea_island_lottery.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class EventController {

    private final EventService eventService;
    private final EntryRepository entryRepository;

    @Autowired
    public EventController(EventService eventService, EntryRepository entryRepository) {
        this.eventService = eventService;
        this.entryRepository = entryRepository;
    }

    // トップページ（イベント一覧）
    @GetMapping("/")
    public String index(Model model) {
        List<EventDto> events = eventService.findAllEventsForList();
        model.addAttribute("events", events);
        return "index";
    }

    // イベント一覧
    @GetMapping("/events")
    public String listEvents(Model model) {
        return index(model);
    }

    // イベント詳細表示
    @GetMapping("/events/{id}")
    public String eventDetail(@PathVariable Long id,
                              @RequestParam(required = false) Boolean completed,
                              //modelはコントローラーからビュー（今回は detail.html）へデータを渡すための入れ物のような役割
                              Model model) {
        Event event = eventService.findEventById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        EventDto eventDto = eventService.convertToDto(event);
        model.addAttribute("event", eventDto);

        // 現在のユーザーの応募情報を取得（テスト用ユーザーIDを使用）
        UUID currentUserId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Optional<Entry> userEntry = entryRepository.findByUserIdAndEventId(currentUserId, id);
        userEntry.ifPresent(entry -> model.addAttribute("userEntry", entry));

        // ユーザーが他のイベントに応募中かどうかのフラグ
        boolean hasActiveEntry = entryRepository.existsByUserIdAndStatus(currentUserId, "WAITING");
        //model(入れ物)へ addAttribute でtrue または false の値を格納 => hasActiveEntry が boolean型な為
        model.addAttribute("hasActiveEntry", hasActiveEntry);

        // 応募完了フラグがあればモデルに追加
        if (Boolean.TRUE.equals(completed)) {
            model.addAttribute("showCompletionModal", true);
        }
        return "event/detail";
    }
}
