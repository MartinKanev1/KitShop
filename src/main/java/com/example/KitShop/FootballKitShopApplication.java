package com.example.KitShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class FootballKitShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootballKitShopApplication.class, args);
	}

}
