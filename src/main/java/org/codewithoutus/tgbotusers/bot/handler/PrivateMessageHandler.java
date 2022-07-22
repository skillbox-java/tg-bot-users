package org.codewithoutus.tgbotusers.bot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.bot.UpdateUtils;
import org.codewithoutus.tgbotusers.bot.service.TelegramService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrivateMessageHandler extends Handler {

    private final TelegramService telegramService;
    private static final String SORRY = "Sorry, wrong or unknown command";

    @Override
    protected boolean handle(Update update) {
        if (!UpdateUtils.isPrivateMessage(update)) {
            return false;
        }

        String text = UpdateUtils.getMessageText(update);
        Long chatId = UpdateUtils.getChat(update).id();

        if (handleForwardMessage(chatId, update) || handleTestRequest(chatId, text)) {
            return true;
        }
        telegramService.sendMessage(new SendMessage(chatId, SORRY));
        return false;
    }

    private boolean handleForwardMessage(Long chatId, Update update) {
        if (!UpdateUtils.isForwardMessage(update)) {
            return false;
        }
        Integer messageId = update.message().messageId();
        User user = telegramService.getUser(chatId, chatId);
        String userName = user.firstName().isBlank() ? user.username() : user.firstName();
        String text = userName + ", сплетничать не хорошо";
        telegramService.sendMessage(new SendMessage(chatId, text).replyToMessageId(messageId));
        return true;
    }

    private boolean handleTestRequest(Long chatId, String text) {
        if (text.startsWith("/test ")) {
            telegramService.sendMessage(new SendMessage(chatId, "Идёт тест..."));
            if (text.startsWith("/test InlineKeyboard")) {
                String callbackData = text.substring("/test InlineKeyboard".length() + 1).trim();
                if (!callbackData.isBlank()) {
                    InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(
                            new InlineKeyboardButton("callback_data").callbackData(callbackData));
                    telegramService.sendMessage(new SendMessage(chatId, "InlineKeyboard test").replyMarkup(keyboard));
                }
            } else {
                telegramService.sendMessage(new SendMessage(chatId, SORRY));
            }
            return true;
        }
        return false;
    }
}