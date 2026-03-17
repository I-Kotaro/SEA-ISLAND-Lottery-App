package com.example.sea_island_lottery.controller;

import com.example.sea_island_lottery.dto.EventDto;
import com.example.sea_island_lottery.entity.Entry;
import com.example.sea_island_lottery.entity.User;
import com.example.sea_island_lottery.repository.EntryRepository;
import com.example.sea_island_lottery.service.EventService;
import com.example.sea_island_lottery.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.security.Security;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    private final UserService userService;
    private final EventService eventService;
    private final EntryRepository entryRepository;

    @Autowired
    public MyPageController(UserService userService, EventService eventService, EntryRepository entryRepository) {
        this.userService = userService;
        this.eventService = eventService;
        this.entryRepository = entryRepository;
    }

    @GetMapping
    public String index(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);

        // 応募中のイベントを取得し、DTOに変換
        List<Entry> entries = entryRepository.findByUserId(user.getId());
        List<EventDto> waitingEvents = entries.stream()
                .filter(entry -> "WAITING".equals(entry.getStatus()))
                .map(entry -> eventService.convertToDto(entry.getEvent()))
                .collect(Collectors.toList());
        model.addAttribute("waitingEvents", waitingEvents);
        return "mypage/index";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute User userForm, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        User currentUser = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User user = new User();
        user.setName(userForm.getName());
        userService.updateUser(currentUser.getId(), user);
        return "redirect:/mypage";
    }

    @PostMapping("/delete")
    public String delete(Principal principal, HttpServletRequest request) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        User currentUser = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ユーザーを削除
        userService.deleteUser(currentUser.getId());

        // ログアウト処理：セキュリティコンテキストのクリアとセッションの無効化
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }

}
