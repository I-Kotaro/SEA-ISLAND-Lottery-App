package com.example.sea_island_lottery.controller;

import com.example.sea_island_lottery.entity.User;
import com.example.sea_island_lottery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ユーザープロフィール表示
    @GetMapping("/{id}")
    public String userProfile(@PathVariable UUID id, Model model) {
        User user = userService.findUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        model.addAttribute("user", user);
        return "user/profile"; // src/main/resources/templates/user/profile.html を表示
    }
}
