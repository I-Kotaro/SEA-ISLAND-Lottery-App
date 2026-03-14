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

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/{id}")
    public String showTicket(@PathVariable("id") Long id, Model model) {
        TicketDto ticketDto = ticketService.getTicketInfo(id);

        if (ticketDto == null) {
            // エラーページにリダイレクトするか、エラーメッセージを表示
            return "redirect:/";
        }

        model.addAttribute("entryId", ticketDto.getEntryId());
        model.addAttribute("userName", ticketDto.getUserName());
        model.addAttribute("eventName", ticketDto.getEventName());
        model.addAttribute("queueNumber", ticketDto.getQueueNumber());
        model.addAttribute("waitTime", ticketDto.getWaitTime());

        return "ticket/index";
    }
}
