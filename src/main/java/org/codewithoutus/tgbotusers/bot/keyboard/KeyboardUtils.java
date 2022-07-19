package org.codewithoutus.tgbotusers.bot.keyboard;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import org.codewithoutus.tgbotusers.config.AppStaticContext;

import java.util.Arrays;
import java.util.Optional;

public class KeyboardUtils {

    public static <E extends Keyboard> InlineKeyboardMarkup createKeyboard(Class<E> enumClass, String callbackDataId) {
        return new InlineKeyboardMarkup(Arrays
                .stream(enumClass.getEnumConstants())
                .map(value -> new InlineKeyboardButton(value.getRepresentation()).callbackData(
                        AppStaticContext.OBJECT_MAPPER
                                .createObjectNode()
                                .put("command", value.getBotCommand().getText())
                                .put(AppStaticContext.CALLBACK_QUERY_DATA_ID_FIELD, callbackDataId)
                                .toString()))
                .toArray(InlineKeyboardButton[]::new));
    }

    public static <E extends Keyboard> Optional<E> defineKey(Class<E> enumClass, String command) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(value -> command.startsWith(value.getBotCommand().getText()))
                .findFirst();
    }
}