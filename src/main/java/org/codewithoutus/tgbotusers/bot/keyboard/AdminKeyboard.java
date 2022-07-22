package org.codewithoutus.tgbotusers.bot.keyboard;

import lombok.Getter;
import org.codewithoutus.tgbotusers.bot.enums.BotCommand;

@Getter
public enum AdminKeyboard implements Keyboard {

    HELP(BotCommand.HELP, "Помощь"),
    CURRENT_SETTINGS(BotCommand.CURRENT_SETTINGS, "Текущие настройки"),

    ADD_MODER_CHAT(BotCommand.ADD_MODER_CHAT, "Добавить чат модераторов"),
    ADD_USER_CHAT(BotCommand.ADD_USER_CHAT, "Добавить чат пользователей"),

    DELETE_MODER_CHAT(BotCommand.DELETE_MODER_CHAT, "Удалить чат модераторов"),
    DELETE_USER_CHAT(BotCommand.DELETE_USER_CHAT, "Удалить чат пользователей"),

    BIND_USER_CHAT_TO_MODER(BotCommand.BIND_USER_CHAT_TO_MODER, "Привязать чат пользователей к модераторам"),
    UNBIND_USER_CHAT_FROM_MODER(BotCommand.UNBIND_USER_CHAT_FROM_MODER, "Отвязать чат пользователей к модераторам");

    private final BotCommand botCommand;
    private final String representation;

    AdminKeyboard(BotCommand botCommand, String representation) {
        this.botCommand = botCommand;
        this.representation = representation;
    }
}