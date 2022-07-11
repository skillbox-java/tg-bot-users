package org.codewithoutus.tgbotusers.controller;

import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.dto.BackendResponse;
import org.codewithoutus.tgbotusers.service.BotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backend")
@RequiredArgsConstructor
public class BotController {

    private final BotService botService;

    @GetMapping("/start")
    private BackendResponse startBotBackend() {
        return botService.start();
    }

    @GetMapping("/stop")
    private BackendResponse stopBotBackend() {
        return botService.stop();
    }

    @GetMapping("/sendMessage")
    private String sendMessage(@RequestParam String message) {
        botService.sendMessage(-644481529L, message);
        return "Message send";
    }
}
