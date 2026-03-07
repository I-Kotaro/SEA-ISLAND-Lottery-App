package com.example.sea_island_lottery.controller;

import com.example.sea_island_lottery.entity.User;
import com.example.sea_island_lottery.service.EntryService;
import com.example.sea_island_lottery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequestMapping("/entries")
public class EntryController {

    private final EntryService entryService;
    private final UserService userService;

    @Autowired
    public EntryController(EntryService entryService, UserService userService) {
        this.entryService = entryService;
        this.userService = userService;
    }

    // イベントへの応募処理
    @PostMapping("/create")
    public String createEntry(@RequestParam("eventId") Long eventId,
                              @RequestParam("userId") UUID userId, // 本来はセッションから取得
                              Model model) {
        try {
            // ユーザーを取得
            User user = userService.findUserById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 応募処理を実行
            entryService.createEntry(user, eventId);

            return "redirect:/events/" + eventId + "?success"; // 成功したらイベント詳細ページへリダイレクト
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error"; // エラーページへ（簡易的）
        }
    }
}
