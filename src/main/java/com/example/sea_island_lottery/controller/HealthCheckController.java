package com.example.sea_island_lottery.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Render等のプラットフォームでスリープを防止するためのヘルスチェック用コントローラー。
 * 定期的にリクエストを受けることでインスタンスの稼働を維持します。
 */
@RestController
public class HealthCheckController {

    /**
     * ヘルスチェック用エンドポイント。
     * @return サーバーが稼働中であることを示す文字列 "OK"
     */
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
