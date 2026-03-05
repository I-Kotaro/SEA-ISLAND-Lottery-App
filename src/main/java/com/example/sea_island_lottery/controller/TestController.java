package com.example.sea_island_lottery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class TestController {

    @GetMapping("/test")
    public String testTailwind() {
        return "test"; // src/main/resources/templates/test.html を参照
    }
}
