package com.example.sea_island_lottery.controller;

import com.example.sea_island_lottery.dto.EventDto;
import com.example.sea_island_lottery.dto.TicketDto;
import com.example.sea_island_lottery.entity.User;
import com.example.sea_island_lottery.service.TicketService;
import com.example.sea_island_lottery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;
    private final UserService userService;

    @Autowired
    public TicketController(TicketService ticketService, UserService userService) {
        this.ticketService = ticketService;
        this.userService = userService;
    }

    //ログイン中のユーザーの最新チケット情報を表示
    //Principal => ログインしているユーザーを表すインターフェース
    @GetMapping("")
    public String showMyTicket(java.security.Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        TicketDto ticketDto = ticketService.getLatestWaitingTicketByUserId(user.getId());

        //DTO使用時必須変換
        if (ticketDto == null) {
            model.addAttribute("noActiveEntry", true);
        } else {
            model.addAttribute("noActiveEntry", false);
            model.addAttribute("entryId", ticketDto.getEntryId());
            model.addAttribute("userName", ticketDto.getUserName());
            model.addAttribute("eventName", ticketDto.getEventName());
            model.addAttribute("queueNumber", ticketDto.getQueueNumber());
            model.addAttribute("waitTime", ticketDto.getWaitTime());
        }

        //ticketフォルダ内index.html
        return "ticket/index";
    }

    @GetMapping("/{id}")
    public String showTicket(@PathVariable("id") Long id, Model model) {
        TicketDto ticketDto = ticketService.getTicketInfo(id);

        if (ticketDto == null) {
            // エラーページにリダイレクトするか、エラーメッセージを表示
            return "redirect:/";
        }

        //DTO使用時必須変換
        //Thymeleafで <span th:text="${userName}"></span> と使える
        model.addAttribute("entryId", ticketDto.getEntryId());
        model.addAttribute("userName", ticketDto.getUserName());
        model.addAttribute("eventName", ticketDto.getEventName());
        model.addAttribute("queueNumber", ticketDto.getQueueNumber());
        model.addAttribute("waitTime", ticketDto.getWaitTime());

        return "ticket/index";
    }
}
