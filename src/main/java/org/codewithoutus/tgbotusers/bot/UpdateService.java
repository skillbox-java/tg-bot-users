package org.codewithoutus.tgbotusers.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateService {

    private final ObjectMapper objectMapper;

    public Boolean isPrivateMessage(Update update) {
        return Optional.ofNullable(update.message())
                .map(Message::chat)
                .map(chat -> chat.type() == Chat.Type.Private)
                .orElse(Boolean.FALSE);
    }

    public Long getChatId(Update update) {
        return Optional.ofNullable(update.message())
                .map(Message::chat)
                .map(Chat::id)
                .orElse(null);
    }

    public Map<String, String> getCallbackData(Update update) {
        return Optional
                .ofNullable(update.callbackQuery())
                .map(CallbackQuery::data)
                .filter(data -> !data.isBlank())
                .map(data -> {
                    try {
                        return objectMapper.readValue(data, new TypeReference<Map<String, String>>() {
                        });
                    } catch (JsonProcessingException e) {
                        log.error("CallbackQuery json parsing from update {}", this);
                        return null;
                    }
                })
                .orElse(null);
    }
}