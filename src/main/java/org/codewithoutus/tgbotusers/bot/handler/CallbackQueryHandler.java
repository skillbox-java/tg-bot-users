package org.codewithoutus.tgbotusers.bot.handler;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codewithoutus.tgbotusers.bot.exception.CommandNotFoundException;
import org.codewithoutus.tgbotusers.bot.keyboard.CongratulationDecisionKeyboard;
import org.codewithoutus.tgbotusers.bot.keyboard.KeyboardUtils;
import org.codewithoutus.tgbotusers.bot.service.NotificationService;
import org.codewithoutus.tgbotusers.config.AppStaticContext;
import org.codewithoutus.tgbotusers.model.entity.UserJoining;
import org.codewithoutus.tgbotusers.model.enums.CongratulateStatus;
import org.codewithoutus.tgbotusers.model.service.UserJoiningService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CallbackQueryHandler extends Handler {

    private final NotificationService notificationService;
    private final UserJoiningService userJoiningService;

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

    @Transactional
    private boolean handleCongratulationDecision(String command, Map<String, Object> callbackQueryData) {
        Optional<CongratulationDecisionKeyboard> key = KeyboardUtils.defineKey(CongratulationDecisionKeyboard.class, command);
        if (key.isEmpty()) {
            return false;
        }
        CongratulationDecisionKeyboard decision = key.get();
        Integer userJoiningId = (Integer) callbackQueryData.get(AppStaticContext.CALLBACK_QUERY_DATA_ID_FIELD);
        UserJoining userJoining = userJoiningService.findById(userJoiningId)
                .orElseThrow(() -> new IllegalStateException("CallbackQueryData with no exist user joining ID" + userJoiningId));

        Long chatId = userJoining.getChatId();
        Long userId = userJoining.getUserId();
        Integer anniversaryNumber = userJoining.getAnniversaryNumber();

        // TODO: Pavel (подумать) -- если из списка, то можно поздравить отклоненного раннее, а иначе нельзя
        if (userJoiningService.existCongratulatedUser(chatId, anniversaryNumber)) {
            return true;
        }

        CongratulateStatus newStatus = (decision == CongratulationDecisionKeyboard.CONGRATULATE)
                ? CongratulateStatus.CONGRATULATE
                : CongratulateStatus.DECLINE;
        userJoining.setStatus(newStatus);
//        userJoiningService.save(userJoining); // TODO: Алекс -- check, that entity was updated without save() method

        if (decision == CongratulationDecisionKeyboard.CONGRATULATE) {
            notificationService.deleteKeyboardFromAllJoiningNotifications(chatId, anniversaryNumber);
            notificationService.notifyUserAboutAnniversaryJoining(userJoining);

        } else if (decision == CongratulationDecisionKeyboard.DECLINE) {
            notificationService.deleteKeyboardFromJoiningNotification(userId, chatId, anniversaryNumber);
        }
        return true;
    }

    @Transactional
    private boolean handleProvidingAnniversaryStatistic(String command, Map<String, Object> callbackQueryData) {
        // TODO: Алекс -- реализовать вывод списка счастивчиков "/списокЮбилейный" или "/luckyList"

        // TODO: Алекс -- уже поздравленные имеют корону, можно добавить в шаблоны и вставлять в начало, неудачники не показываются
        // TODO: Алекс -- если на anniversary number не было поздравления, то выводятся все претенденты с кнопками CongratulationDecisionKeyboard
        // TODO: Алекс -- если поздравленного нет, то отклоненные тоже выводятся
        // TODO: Алекс -- все новые кнопки нужно записывать в таблицу UserJoiningNotification, чтобы потом также удалялись
        // TODO: Алекс -- можно попробовать выводить все-все чаты, а потом доработать -- на будущее
        return false;
    }
}