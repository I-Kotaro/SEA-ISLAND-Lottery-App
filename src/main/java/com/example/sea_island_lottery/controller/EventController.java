package com.example.sea_island_lottery.controller;

import com.example.sea_island_lottery.dto.EventDto;
import com.example.sea_island_lottery.entity.Entry;
import com.example.sea_island_lottery.entity.Event;
import com.example.sea_island_lottery.entity.User;
import com.example.sea_island_lottery.repository.EntryRepository;
import com.example.sea_island_lottery.service.EventService;
import com.example.sea_island_lottery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class EventController {

    private final EventService eventService;
    private final EntryRepository entryRepository;
    private final UserService userService;

    //DI注入
    //DI注入 => エンティティへつなぐ回路を作るイメージ
    //もしeventコントローラーからuserエンティティにアクセスさせたい際はuserサービスへＤＩ注入する
    @Autowired
    public EventController(EventService eventService, EntryRepository entryRepository, UserService userService) {
        this.eventService = eventService;
        this.entryRepository = entryRepository;
        this.userService = userService;
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
    public String eventDetail(@PathVariable("id") Long id,
                              @RequestParam(value = "completed", required = false) Boolean completed,
                              jakarta.servlet.http.HttpServletRequest request,
                              Model model) {
        Principal principal = request.getUserPrincipal();

        Event event = eventService.findEventById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        EventDto eventDto = eventService.convertToDto(event);
        model.addAttribute("event", eventDto);

        if (principal != null) {
            String email = principal.getName();
            User currentUser = userService.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            UUID currentUserId = currentUser.getId();

            // 現在のユーザーの応募情報を取得
            Optional<Entry> userEntry = entryRepository.findByUserIdAndEventId(currentUserId, id);
            userEntry.ifPresent(entry -> model.addAttribute("userEntry", entry));

            // ユーザーが他のイベントに応募中かどうかのフラグ（このイベント以外のWAITINGがあるか）
            boolean hasActiveEntry = entryRepository.existsByUserIdAndStatusAndEventIdNot(currentUserId, "WAITING", id);
            model.addAttribute("hasActiveEntry", hasActiveEntry);
        } else {
            model.addAttribute("hasActiveEntry", false);
        }

        // 応募完了フラグがあればモデルに追加
        if (Boolean.TRUE.equals(completed)) {
            model.addAttribute("showCompletionModal", true);
        }
        return "event/detail";

    }
}
