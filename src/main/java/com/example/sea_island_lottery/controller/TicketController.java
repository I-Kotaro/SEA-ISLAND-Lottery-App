package com.example.sea_island_lottery.controller;

import com.example.sea_island_lottery.dto.TicketDto;
import com.example.sea_island_lottery.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    //ここで次呼ぶフォルダを TicketService 指定
    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    //@PathVariable => URLにあるパス値(id)を取得するアノテーション
    //Model model => HTMLにデータを渡す箱(addAttributeで使う)
    @GetMapping("/{id}")
    public String showTicket(@PathVariable("id") Long id, Model model) {
        TicketDto ticketDto = ticketService.getTicketInfo(id);

        if (ticketDto == null) {
            // エラーページにリダイレクトするか、エラーメッセージを表示
            return "redirect:/";
        }

        //Thymeleafで <span th:text="${userName}"></span> と使える
        model.addAttribute("entryId", ticketDto.getEntryId());
        model.addAttribute("userName", ticketDto.getUserName());
        model.addAttribute("eventName", ticketDto.getEventName());
        model.addAttribute("queueNumber", ticketDto.getQueueNumber());
        model.addAttribute("waitTime", ticketDto.getWaitTime());

        return "ticket/index";
    }
}
