package org.codewithoutus.tgbotusers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TgBotUsersApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(TgBotUsersApplication.class, args);
		context.start();
	}
}