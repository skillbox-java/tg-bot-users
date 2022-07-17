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
import org.codewithoutus.tgbotusers.model.repository.ChatUserRepository;
import org.codewithoutus.tgbotusers.model.service.ChatModeratorService;
import org.codewithoutus.tgbotusers.model.service.ChatUserService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PrivateMessageHandler extends Handler {
    //список команд
    //todo надо потом сделать привествие бота,что бы он сразу присал что если что есть help
    private static final String HELP = "/help";
    private static final String ADD_MODER_CHAT = "/addModerChat";
    private static final String ADD_USER_CHAT = "/addUserChat";
    private static final String BIND_USER_CHAT_TO_MODER = "/bindUserChatToModer";
    private static final String OK_BIND = "Cool, user chat № %s , is add in moder chat № %s";
    private static final String OK_MODER = "Cool, moder chat № %s , is add in DB";
    private static final String OK_USER = "Cool, user chat № %s , is add in DB";
    private static final String NOT_MODER = "Oh, moder chat  № %s ,is already in the database";
    private static final String NOT_USER = "Oh, user chat  № %s ,is already in the database";
    private static final String ERROR = "Sorry, \" %s\" ,is invalid format ID";
    private static final String SORRY = "Sorry, bot does not know this  \"%s\" command";
    List<String> listCommandAdd = Arrays.asList(HELP, ADD_MODER_CHAT, ADD_USER_CHAT, BIND_USER_CHAT_TO_MODER);
    List<String> listCommandDelete = Arrays.asList(ADD_MODER_CHAT, ADD_USER_CHAT, BIND_USER_CHAT_TO_MODER);


    private final TelegramService telegramService;
    private final ChatModeratorService chatModeratorService;
    private final ChatUserService chatUserService;


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

        Chat chatFrom = UpdateUtils.getChat(update);//получаем чат из которого писали боту
        String command = text.split(" ")[0];
        switch (command) {
            case HELP -> {
                telegramService.sendMessage(new SendMessage(chatFrom.id(), "Список доступных команд :\n"
                        + listCommandAdd.toString() + "\n" + listCommandDelete));
                return true;
            }
            case ADD_MODER_CHAT -> {
                addModerChat(update, chatId, text);
                return true;
            }
            case ADD_USER_CHAT -> {
                addUserChat(update, chatId, text);
                return true;
            }
            case BIND_USER_CHAT_TO_MODER -> {
                bindUserChatToModer(update, text);
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
                    telegramService.sendMessage(new SendMessage(chatFrom.id(), String.format(OK_MODER, chatIdForAdd)));
                    rep.save(new ChatModerator(chatIdForAdd, new ArrayList<>()));//сохраняем группу до перезагрузки бота
                    return true;
                } else {
                    telegramService.sendMessage(new SendMessage(chatFrom.id(), String.format(NOT_MODER, chatIdForAdd)));
                    return true;
                }
            }
            telegramService.sendMessage(new SendMessage(chatFrom.id(), String.format(ERROR, chatIdForAdd)));
            return true;
        }
        return false;
    }

    private boolean addUserChat(Update update, Long chatId, String text) {
        long chatIdForAdd = Integer.parseInt(text.substring(ADD_USER_CHAT.length()).trim());//парсим id группы
        Chat chatFrom = UpdateUtils.getChat(update);//получаем чат из которого писали боту
        if (chatIdForAdd > 0) {
            ChatUserRepository rep = chatUserService.getChatUserRepository();
            if (rep.findByChatId(chatIdForAdd) == null) {
                telegramService.sendMessage(new SendMessage(chatFrom.id(), String.format(OK_USER, chatIdForAdd)));
                rep.save(new ChatUser(chatIdForAdd, new ArrayList<>()));//сохраняем группу до перезагрузки бота
                return true;
            } else {
                telegramService.sendMessage(new SendMessage(chatFrom.id(), String.format(NOT_USER, chatIdForAdd)));
                return true;
            }
        }
        return false;
    }

    private boolean bindUserChatToModer(Update update, String text) {
        Chat chatFrom = UpdateUtils.getChat(update);//получаем чат из которого писали боту
        int count = text.split(" ").length;
        if (count == 3) {
            ChatUserRepository repUser = chatUserService.getChatUserRepository();
            int idUser = Integer.parseInt(text.split(" ")[1]);
            if (repUser.findByChatId(idUser) != null) {
                ChatModeratorRepository repModer = chatModeratorService.getChatModeratorRepository();
                long idModer = Integer.parseInt(text.split(" ")[2]);
                if (!repModer.findByChatId(idModer).isEmpty()) {
                    repModer.findByChatId(idModer).get().addChatUser(repUser.findByChatId(idUser));
                    telegramService.sendMessage(new SendMessage(chatFrom.id(), String.format(OK_BIND, idUser, idModer)));
                    return true;
                }
                telegramService.sendMessage(new SendMessage(chatFrom.id(), String.format(NOT_MODER, idModer)));
                return true;
            }
            telegramService.sendMessage(new SendMessage(chatFrom.id(), String.format(NOT_USER, idUser)));
            return true;
        }
        telegramService.sendMessage(new SendMessage(chatFrom.id(), String.format(SORRY, text + "-" + count)));
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