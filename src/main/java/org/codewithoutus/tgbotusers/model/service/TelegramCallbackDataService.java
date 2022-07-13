package org.codewithoutus.tgbotusers.model.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codewithoutus.tgbotusers.bot.exception.CallbackDataMappingException;
import org.codewithoutus.tgbotusers.config.AppStaticContext;
import org.codewithoutus.tgbotusers.model.entity.TelegramCallbackData;
import org.codewithoutus.tgbotusers.model.repository.TelegramCallbackDataRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramCallbackDataService {

    private final TelegramCallbackDataRepository telegramCallbackDataRepository;

    public TelegramCallbackData save(TelegramCallbackData telegramCallbackData) {
        return telegramCallbackDataRepository.save(telegramCallbackData);
    }

    public TelegramCallbackData findById(int id) {
        return telegramCallbackDataRepository.findById(id);
    }

    public TelegramCallbackData saveMap(Map<String, Object> callbackData) {
        try {
            String json = AppStaticContext.OBJECT_MAPPER.writeValueAsString(callbackData);
            TelegramCallbackData entity = new TelegramCallbackData();
            entity.setData(json);
            return save(entity);

        } catch (JsonProcessingException e) {
            log.error("Serializing error of Map {}", callbackData);
            throw new CallbackDataMappingException(callbackData.toString());
        }
    }
}