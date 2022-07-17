package org.codewithoutus.tgbotusers.bot.handler;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.bot.service.TelegramService;
import org.codewithoutus.tgbotusers.model.entity.ChatModerator;
import org.codewithoutus.tgbotusers.model.entity.ChatUser;
import org.codewithoutus.tgbotusers.model.repository.ChatModeratorRepository;
import org.codewithoutus.tgbotusers.model.service.ChatModeratorService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PrivateMessageHandler extends Handler {
    //список команд
    private static final String HELP = "/help";
    private static final String ADD_MODER_CHAT = "/addModerChat";
    private static final String ADD_USER_CHAT = "/addUserChat";
    private static final String ADD_USER_CHAT_IN_MODER_CHAT = "/addUserChatInModerChat";
    private static final String ADD = "Cool, moder chat № %s , is add in DB";
    private static final String NOT = "Oh, moder chat  № %s ,is already in the database";
    private static final String ERROR = "Sorry, \" %s\" ,is invalid format ID";
    private static final String SORRY = "Sorry, bot does not know this  \"%s\" command";
    List<String> listCommand= Arrays.asList(HELP,ADD_MODER_CHAT,ADD_USER_CHAT,ADD_USER_CHAT_IN_MODER_CHAT);


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
                || handleAdminRequest(update, chatId, text)
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
        telegramService.sendMessage(new SendMessage(chatId, String.format(SORRY, text)));
        return true;
    }

    private boolean handleAdminRequest(Update update, Long chatId, String text) {
        // TODO: Макс -- реализовать админку (добавление и удаление чатов)
        // TODO: Макс -- вначале строковыми командами, а потом можно попробовать кнопки
        // TODO: Макс -- изменения настроек должны сохраняться в базе

        //мой id=161855902  /11725
        //TODO проверка пришла ли команда от админа или от левого,если от админа дальше иначе брек
        //TODO получить номер группы
        //TODO добавить группу в БД
        Chat chatFrom = UpdateUtils.getChat(update);//получаем чат из которого писали боту
        String command = text.split(" ")[0];
        switch (command) {
            case HELP -> {
                telegramService.sendMessage(new SendMessage(chatFrom.id(), "Список доступных команд :\n"+listCommand.toString()));
                return true;
            }
            case ADD_MODER_CHAT -> {
                addModerChat(update, chatId, text);

                return true;
            }
            case ADD_USER_CHAT -> {
                telegramService.sendMessage(new SendMessage(chatFrom.id(), "добавили юсер чат"));
                return true;
            }
            case ADD_USER_CHAT_IN_MODER_CHAT -> {
                telegramService.sendMessage(new SendMessage(chatFrom.id(), "добавили юсер чат в модер чат"));
                return true;
            }


        }
        return false;
    }

    private boolean addModerChat(Update update, Long chatId, String text) {
        if (text.startsWith(ADD_MODER_CHAT)) {//парсим команду
            long chatIdForAdd = Integer.parseInt(text.substring(ADD_MODER_CHAT.length()).trim());//парсим id группы
            Chat chatFrom = UpdateUtils.getChat(update);//получаем чат из которого писали боту

            if (chatIdForAdd > 0) {//проверяем на валидность номер чата
                ChatModeratorRepository rep = chatModeratorService.getChatModeratorRepository();
                if ((rep.findByChatId(chatIdForAdd).isEmpty())) {
                    telegramService.sendMessage(new SendMessage(chatFrom.id(), String.format(ADD, chatIdForAdd)));
                    rep.save(new ChatModerator(chatIdForAdd, new ArrayList<>()));//сохраняем группу до перезагрузки бота
                    return true;
                } else {
                    System.out.println(rep.findByChatId(chatIdForAdd).toString());
                    telegramService.sendMessage(new SendMessage(chatFrom.id(), String.format(NOT, chatIdForAdd)));
                    return true;
                }
            }
            telegramService.sendMessage(new SendMessage(chatFrom.id(), String.format(ERROR, chatIdForAdd)));
            return true;
        }
        return false;
    }

    private boolean addUserChat() {

        return true;
    }

    private boolean addUserChatInModerChat(int chatModerator, int chatUser) {
        ChatModerator chat = new ChatModerator();
        chat.addChatUser(new ChatUser());
        return true;
    }
    private boolean deleteUserChat() {

        return true;
    }
    private boolean deleteModerChat() {

        return true;
    }
    private boolean deleteUserChatFromModerChat() {

        return true;
    }
}