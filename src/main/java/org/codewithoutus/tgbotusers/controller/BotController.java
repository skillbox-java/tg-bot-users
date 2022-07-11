package org.codewithoutus.tgbotusers.controller;

import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.config.BotConfig;
import org.codewithoutus.tgbotusers.config.GroupConfig;
import org.codewithoutus.tgbotusers.config.NotificationConfig;
import org.codewithoutus.tgbotusers.dto.BackendResponse;
import org.codewithoutus.tgbotusers.services.BotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backend")
@RequiredArgsConstructor
public class BotController {
    private final NotificationConfig notificationConfig;
    private final GroupConfig groupConfig;
    private final BotConfig botConfig;
    
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
    private BackendResponse sendMessage(@RequestParam String message) {
        return botService.sendMessage(message);
    }

    @GetMapping("/props")
    private String getProps(){
        return notificationConfig+"\n"+groupConfig+"\n"+botConfig+"\n";

    }
    
}
