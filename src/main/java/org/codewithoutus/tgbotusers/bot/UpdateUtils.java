package org.codewithoutus.tgbotusers.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.pengrad.telegrambot.model.*;
import lombok.extern.slf4j.Slf4j;
import org.codewithoutus.tgbotusers.bot.enums.BotCommand;
import org.codewithoutus.tgbotusers.bot.exception.CallbackDataMappingException;
import org.codewithoutus.tgbotusers.config.AppStaticContext;
import org.codewithoutus.tgbotusers.config.ChatSettings;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class UpdateUtils {

    public static Boolean isPrivateMessage(Update update) {
        return Optional.ofNullable(update.message())
                .map(Message::chat)
                .map(chat -> chat.type() == Chat.Type.Private)
                .orElse(Boolean.FALSE);
    }

    public static boolean isPrivateMessageFromAdmin(Update update, ChatSettings chatSettings) {
        return isPrivateMessage(update) && chatSettings.isAdminId(getUserId(update));
    }

    public static boolean isForwardMessage(Update update) {
        return Optional.ofNullable(update.message())
                .map(Message::forwardDate)
                .isPresent();
    }

    public static String getMessageText(Update update) {
        return Optional.ofNullable(update.message())
                .map(Message::text)
                .orElse("");
    }

    public static User[] getNewChatMembers(Update update) {
        return Optional.ofNullable(update.message())
                .map(Message::newChatMembers)
                .orElse(null);
    }

    public static BotCommand getBotCommand(Update update) {
        return Optional.ofNullable(update.message())
                .map(Message::entities)
                .flatMap(entities -> Arrays.stream(entities)
                        .filter(entity -> entity.type().equals(MessageEntity.Type.bot_command))
                        .findFirst()
                        .map(e -> getMessageText(update).substring(e.offset(), e.length()))
                        .map(BotCommand::getByCommandText))
                .orElse(null);
    }

    public static Chat getChat(Update update) {
        return Optional.ofNullable(update.message())
                .map(Message::chat)
                .orElse(null);
    }

    public static Long getChatId(Update update) {
        Chat chat = getChat(update);
        return chat == null ? null : chat.id();
    }

    public static User getUser(Update update) {
        return Optional.ofNullable(update.message())
                .map(Message::from)
                .orElse(null);
    }

    public static Long getUserId(Update update) {
        User user = getUser(update);
        return user == null ? null : user.id();
    }

    public static String getCallbackQueryData(Update update) {
        return Optional.ofNullable(update.callbackQuery())
                .map(CallbackQuery::data)
                .filter(data -> !data.isBlank())
                .orElse("{}");
    }

    public static Map<String, String> getCallbackQueryDataAsMap(Update update) {
        return getCallbackQueryDataAsMap(getCallbackQueryData(update));
    }

    public static Map<String, String> getCallbackQueryDataAsMap(String callbackData) {
        try {
            return AppStaticContext.OBJECT_MAPPER.readValue(callbackData, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.error("CallbackQuery json parsing to map error {}", callbackData);
            throw new CallbackDataMappingException(callbackData);
        }
    }
}