package org.codewithoutus.tgbotusers.bot.handler;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.bot.service.TelegramService;
import org.codewithoutus.tgbotusers.model.repository.ChatModeratorRepository;
import org.codewithoutus.tgbotusers.model.service.ChatModeratorService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrivateMessageHandler extends Handler {
    //список команд
    private static final String ADD_MODER_CHAT = "/addModerChat";
    private static final String ADD = "Cool, moder chat № %s , is add in DB";
    private static final String NOT = "Oh, moder chat  № %s ,is already in the database";
    private static final String ERROR = "Sorry, \" %s\" ,is invalid format ID";
    private static final String SORRY = "Sorry, bot does not know this  \"%s\" command";


    private final TelegramService telegramService;
    private final ChatModeratorService chatModeratorService;

    @Override
    protected boolean handle(Update update) {
        if (!UpdateUtils.isPrivateMessage(update)) {
            return false;
        }

        String text = UpdateUtils.getMessageText(update);
        Long chatId = UpdateUtils.getChat(update).id();

        if (handleForwardMessage(chatId, update)
                || handleAdminRequest(chatId, text)
                || handleTestRequest(chatId, text)) {
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

    private boolean handleAdminRequest(Long chatId, String text) {
        // TODO: Макс -- реализовать админку (добавление и удаление чатов)
        // TODO: Макс -- вначале строковыми командами, а потом можно попробовать кнопки
        // TODO: Макс -- изменения настроек должны сохраняться в базе
        return false;
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
                    return true;
                }
            }
        }
        //мой id=161855902  /11725
        //TODO проверка пришла ли команда от админа или от левого,если от админа дальше иначе брек
        //TODO получить номер группы
        //TODO добавить группу в БД
        else if (text.startsWith(ADD_MODER_CHAT)) {//парсим команду
            long chatID = Integer.parseInt(text.substring(ADD_MODER_CHAT.length()).trim());//парсим id группы
            Chat chatFrom = UpdateUtils.getChat(update);//получаем чат из которого писали боту
            if (chatID > 0) {//проверяем на валидность номер чата
                //вариант от Алексея
//                if(!chatModeratorService.findByIDModerChatInDatabase(chatID)){//проверяем есть ли этот ид уже в базе
//                    telegramService.sendMessage(new SendMessage(chatFrom.id(), String.format(ADD,chatID)));
                ChatModeratorRepository rep = chatModeratorService.getChatModeratorRepository();
                if ((rep.findByChatId(chatID).toString().matches("Optional.empty"))) {
                    telegramService.sendMessage(new SendMessage(chatFrom.id(), String.format(ADD, chatID)));
                    System.out.println("--------list empty, add ID-----------");
                    System.out.println(rep.findByChatId(chatID).toString());
                    return true;
                } else {
                    System.out.println("--------list NOT empty,NOT add ID-----------");
                    System.out.println(rep.findByChatId(chatID).toString());
                    telegramService.sendMessage(new SendMessage(chatFrom.id(), String.format(NOT, chatID)));
                    return true;
                }
            }
            telegramService.sendMessage(new SendMessage(chatFrom.id(), String.format(ERROR, chatID)));
            return true;
        }

        // TODO: реализовать админку: добавление и удаление чатов

        telegramService.sendMessage(new SendMessage(chat.id(), String.format(SORRY, text)));
        return true;
    }
}