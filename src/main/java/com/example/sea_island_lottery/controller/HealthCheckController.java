package com.example.sea_island_lottery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


// Renderでスリープを防止し、DB接続状態も監視するためのヘルスチェック用コントローラー
@RestController
public class HealthCheckController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * ヘルスチェック用エンドポイント
     * DBに対して軽いクエリを実行し、接続状態を確認
     * @return 状態メッセージ（DB接続成功時は "database: connected" を含む）
     */
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> status = new HashMap<>();

        try {
            // DB接続確認: SELECT 1 を実行
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            status.put("status", "UP");
            status.put("database", "connected");
        } catch (Exception e) {
            status.put("status", "DOWN");
            status.put("database", "disconnected");
            status.put("error", e.getMessage());
        }

        return status;
    }
}
