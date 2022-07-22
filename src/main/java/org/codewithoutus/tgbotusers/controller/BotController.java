package org.codewithoutus.tgbotusers.controller;

import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.bot.service.BotService;
import org.codewithoutus.tgbotusers.bot.service.TelegramService;
import org.codewithoutus.tgbotusers.controller.dto.BotResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("c")
@RequiredArgsConstructor
public class BotController {

    private final BotService botService;
    private final TelegramService telegramService;

    @GetMapping("/start")
    private BotResponse startBotBackend() {
        return new BotResponse(botService.start(), botService.getStatus());
    }

    @GetMapping("/stop")
    private BotResponse stopBotBackend() {
        return new BotResponse(botService.stop(), botService.getStatus());
    }

    @GetMapping("/status")
    private BotResponse getStatus() {
        return new BotResponse(true, botService.getStatus());
    }

    @GetMapping("/sendMessage")
    private BotResponse sendMessage(@RequestParam Long chatId, @RequestParam String message) {
        telegramService.sendMessage(new SendMessage(chatId, message));
        return new BotResponse(true, botService.getStatus());
    }
}