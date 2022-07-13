package org.codewithoutus.tgbotusers.bot.handler;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.bot.service.TelegramService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrivateMessageHandler extends Handler {

    private static final String SORRY = "Sorry, functionality not implemented";
    private final TelegramService telegramService;

    @Override
    protected boolean handle(Update update) {
        if (!UpdateUtils.isPrivateMessage(update)) {
            return false;
        }

        String text = UpdateUtils.getMessageText(update);
        Chat chat = UpdateUtils.getChat(update);

        if (text.startsWith("/test ")) {
            telegramService.sendMessage(new SendMessage(chat.id(), "Идёт тест..."));

            if (text.startsWith("/test InlineKeyboard")) {
                String callbackData = text.substring("/test InlineKeyboard".length() - 1).trim();
                if (!callbackData.isBlank()) {
                    InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(
                            new InlineKeyboardButton("callback_data").callbackData(callbackData));
                    telegramService.sendMessage(new SendMessage(chat.id(), "InlineKeyboard test").replyMarkup(keyboard));
                    return true;
                }
            }
        }

        // TODO: реализовать админку: добавление и удаление чатов

        telegramService.sendMessage(new SendMessage(chat.id(), SORRY));
        return true;
    }
}