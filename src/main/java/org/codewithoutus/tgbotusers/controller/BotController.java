package org.codewithoutus.tgbotusers.controller;

import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.bot.Bot;
import org.codewithoutus.tgbotusers.bot.BotResponse;
import org.codewithoutus.tgbotusers.bot.TelegramService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class BotController {

    private final Bot bot;
    private final TelegramService telegramService;

    @GetMapping("/start")
    private BotResponse startBotBackend() {
        return new BotResponse(bot.start(), bot.getStatus());
    }

    @GetMapping("/stop")
    private BotResponse stopBotBackend() {
        return new BotResponse(bot.stop(), bot.getStatus());
    }

    @GetMapping("/status")
    private BotResponse getStatus() {
        return new BotResponse(true, bot.getStatus());
    }

    @GetMapping("/sendMessage") // chatId = -644481529L
    private BotResponse sendMessage(@RequestParam Long chatId, @RequestParam String message) {
        telegramService.sendMessage(chatId, message);
        return new BotResponse(true, bot.getStatus());
    }
}