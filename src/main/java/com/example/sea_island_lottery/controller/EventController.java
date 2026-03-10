package com.example.sea_island_lottery.controller;

import com.example.sea_island_lottery.dto.EventDto;
import com.example.sea_island_lottery.entity.Event;
import com.example.sea_island_lottery.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // トップページ（イベント一覧）
    //イベント一覧表示はDTOで取得パターン
    @GetMapping("/")
    public String index(Model model) {
        List<EventDto> events = eventService.findAllEventsForList();
        //model.addAttribute => Controller から View にデータを渡すためのメソッド
        model.addAttribute("events", events);
        return "index"; // src/main/resources/templates/index.html を表示
    }

    // イベント一覧（/events でもアクセス可能にする場合）
    @GetMapping("/events")
    public String listEvents(Model model) {
        return index(model); // indexメソッドを再利用
    }

    // イベント詳細表示
    //イベント詳細表示はエンティティで取得パターン
    @GetMapping("/events/{id}")
    public String eventDetail(@PathVariable Long id, Model model) {
        Event event = eventService.findEventById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        EventDto eventDto = eventService.convertToDto(event);
        model.addAttribute("event", eventDto); //addAttribute => controllerからviewにデータを渡すメソッド
        return "event/detail"; // src/main/resources/templates/event/detail.html を表示
    }
}
