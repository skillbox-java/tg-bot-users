package org.codewithoutus.tgbotusers.bot.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codewithoutus.tgbotusers.bot.exception.CommandNotFoundException;
import org.codewithoutus.tgbotusers.bot.keyboard.CongratulationDecisionKeyboard;
import org.codewithoutus.tgbotusers.bot.keyboard.KeyboardUtils;
import org.codewithoutus.tgbotusers.bot.service.NotificationService;
import org.codewithoutus.tgbotusers.config.AppStaticContext;
import org.codewithoutus.tgbotusers.model.entity.TelegramCallbackData;
import org.codewithoutus.tgbotusers.model.entity.UserJoining;
import org.codewithoutus.tgbotusers.model.enums.CongratulateStatus;
import org.codewithoutus.tgbotusers.model.service.TelegramCallbackDataService;
import org.codewithoutus.tgbotusers.model.service.UserJoiningService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CallbackQueryHandler extends Handler {

    private final NotificationService notificationService;
    private final UserJoiningService userJoiningService;
    private final TelegramCallbackDataService telegramCallbackDataService;

    @Override
    protected boolean handle(Update update) {
        Map<String, Object> callbackQueryData = UpdateUtils.getCallbackQueryDataAsMap(update);
        if (callbackQueryData == null || callbackQueryData.isEmpty()) {
            return false;
        }
        String command = (String) callbackQueryData.get("command");
        if (command == null || command.isBlank()) {
            return false;
        }

        if (handleCongratulationDecision(command, callbackQueryData)
                || handleProvidingAnniversaryStatistic(command, callbackQueryData)) {
            return true;

        } else {
            log.error("Unhandled command: {}", callbackQueryData);
            throw new CommandNotFoundException("Unhandled command: " + callbackQueryData);
        }
    }

    private boolean handleCongratulationDecision(String command, Map<String, Object> callbackQueryData) {
        // TODO: доделать реакцию на команду модераторов "поздравить" или "отклонить"
        Optional<CongratulationDecisionKeyboard> key = KeyboardUtils.defineKey(CongratulationDecisionKeyboard.class, command);
        if (key.isEmpty()) {
            return false;
        }
        CongratulationDecisionKeyboard decision = key.get();
        Map<String, Object> callbackData = getCallbackDataFromDatabase(callbackQueryData);

        // TODO: проверить, что данные есть в мапе callbackData
        // TODO: все данные определены в NotificationService.notifyModeratorsAboutUserJoining()
        Long chatId = (Long) callbackData.get("chatId");
        Long userId = (Long) callbackData.get("userId");
        Integer anniversaryNumber = (Integer) callbackData.get("anniversaryNumber");

        // TODO: проверить, что уже не поздравили кого-тотме, иначе отмена
        // TODO: проверить, что кого хотим поздравить в статусе WAIT, иначе отмена

        CongratulateStatus newStatus = (decision == CongratulationDecisionKeyboard.CONGRATULATE)
                ? CongratulateStatus.CONGRATULATE
                : CongratulateStatus.DECLINE;
        UserJoining userJoining = userJoiningService.findByUserIdAndChatId(userId, chatId);
        userJoining.setStatus(newStatus);
        userJoiningService.save(userJoining);

        if (decision == CongratulationDecisionKeyboard.CONGRATULATE) {
            // TODO: удалить кнопки у всех неудачников =)
            // notificationService.deleteKeyboardFromJoiningNotifications(chatId, LIST, anniversaryNumber);
            notificationService.notifyUserAboutJoining(chatId, userId, anniversaryNumber);

        } else {
            notificationService.deleteKeyboardFromJoiningNotification(chatId, userId, anniversaryNumber);
        }
        return true;
    }

    private boolean handleProvidingAnniversaryStatistic(String command, Map<String, Object> callbackQueryData) {
        // TODO: реализовать вывод списка счастивчиков "/списокЮбилейный" или "/luckyList"
        // TODO: уже поздравленные имеют корону, можно добавить в шаблоны и вставлять в начало, неудачники не показываются
        // TODO: если на anniversary number не было поздравления, то выводятся все претенденты с кнопками CongratulationDecisionKeyboard
        // TODO: если поздравленного нет, то отклоненные тоже выводятся
        // TODO: все новые кнопки нужно записывать в таблицу UserJoiningNotification, чтобы потом также удалялись
        // TODO: можно попробовать выводить все-все чаты, а потом доработать -- на будущее
        return false;
    }

    private Map<String, Object> getCallbackDataFromDatabase(Map<String, Object> callbackQueryData) {
        Integer id = (Integer) callbackQueryData.get(AppStaticContext.CALLBACK_QUERY_DATA_ID_FIELD);
        if (id == null) {
            return Collections.emptyMap();
        }
        try {
            TelegramCallbackData telegramCallbackData = telegramCallbackDataService.findById(id);
            if (telegramCallbackData != null) {
                return AppStaticContext.OBJECT_MAPPER.readValue(telegramCallbackData.getData(), new TypeReference<>() {
                });
            }
        } catch (Exception ex) {
            log.error("CallbackData with id {} deserialize error", id);
        }
        return Collections.emptyMap();
    }
}