package org.codewithoutus.tgbotusers.bot.handler;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codewithoutus.tgbotusers.bot.UpdateUtils;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class CallbackQueryHandler extends Handler {

    private final NotificationService notificationService;
    private final UserJoiningService userJoiningService;

    @Override
    protected boolean handle(Update update) {
        // есть ли callbackQuery в update
        Map<String, String> callbackQueryData = UpdateUtils.getCallbackQueryDataAsMap(update);
        if (callbackQueryData == null || callbackQueryData.isEmpty()) {
            return false;
        }

        // есть ли команда в callbackQuery
        String command = callbackQueryData.get("command");
        if (command == null || command.isBlank()) {
            return false;
        }

        if (handleCongratulationDecision(command, callbackQueryData)) {
            return true;
        } else {
            log.error("Unhandled command: {}", callbackQueryData);
            throw new CommandNotFoundException("Unhandled command: " + callbackQueryData);
        }
    }

    @Transactional
    private boolean handleCongratulationDecision(String command, Map<String, String> callbackQueryData) {
        // есть ли команда в callbackQuery
        CongratulationDecisionKeyboard decision = KeyboardUtils
                .defineKey(CongratulationDecisionKeyboard.class, command).orElse(null);
        if (decision == null) {
            return false;
        }

        // есть ли поздравленные в чате с таким порядковым номером
        Integer userJoiningId = Integer.parseInt(callbackQueryData.get(AppStaticContext.CALLBACK_QUERY_DATA_ID_FIELD));
        UserJoining userJoining = userJoiningService.findById(userJoiningId)
                .orElseThrow(() -> new IllegalStateException(
                        "CallbackQueryData with no exist user joining ID" + userJoiningId));

        Long chatId = userJoining.getChatId();
        Long userId = userJoining.getUserId();
        Integer anniversaryNumber = userJoining.getAnniversaryNumber();

        if (userJoiningService.existCongratulatedUser(chatId, anniversaryNumber)) {
            return true;
        }

        CongratulateStatus newStatus = (decision == CongratulationDecisionKeyboard.CONGRATULATE)
                ? CongratulateStatus.CONGRATULATE
                : CongratulateStatus.DECLINE;
        userJoining.setStatus(newStatus);
        userJoiningService.save(userJoining);

        if (decision == CongratulationDecisionKeyboard.CONGRATULATE) {
            notificationService.deleteKeyboardFromAllJoiningNotifications(chatId, anniversaryNumber);
            notificationService.notifyUserAboutAnniversaryJoining(userJoining);

        } else if (decision == CongratulationDecisionKeyboard.DECLINE) {
            notificationService.deleteKeyboardFromJoiningNotification(userId, chatId, anniversaryNumber);
        }
        return true;
    }
}