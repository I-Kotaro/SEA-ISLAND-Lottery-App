package com.example.sea_island_lottery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SeaIslandLotteryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeaIslandLotteryApplication.class, args);
	}

}
