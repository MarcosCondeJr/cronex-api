package com.chronex.cronex_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.chronex.cronex_api.config.DotenvLoader;

@SpringBootApplication
public class CronexApiApplication {

	public static void main(String[] args) {
		DotenvLoader.init();
		SpringApplication.run(CronexApiApplication.class, args);
	}

}
