package org.codewithoutus.tgbotusers.bot.handler;

import com.pengrad.telegrambot.request.SendMessage;
import org.codewithoutus.tgbotusers.bot.enums.BotCommand;
import org.codewithoutus.tgbotusers.bot.service.TelegramService;
import org.codewithoutus.tgbotusers.mocks.answer.AnswerChatModerator;
import org.codewithoutus.tgbotusers.mocks.answer.AnswerSendResponse;
import org.codewithoutus.tgbotusers.mocks.dto.Chat;
import org.codewithoutus.tgbotusers.mocks.dto.Message;
import org.codewithoutus.tgbotusers.mocks.dto.Update;
import org.codewithoutus.tgbotusers.model.entity.ChatModerator;
import org.codewithoutus.tgbotusers.model.service.ChatModeratorService;
import org.codewithoutus.tgbotusers.model.service.ChatUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminMessageHandlerTest {
    @Mock
    private TelegramService telegramService;
    @Mock
    private ChatModeratorService chatModeratorService;
    @Mock
    private ChatUserService chatUserService;

    @InjectMocks
    private AdminMessageHandler adminMessageHandler;

    @DisplayName("Тест добавления чата модераторов: добавление уже имеющегося в базе ID")
    @Test
    public void testAddModerChat_existId() {
        // when
        ChatModerator entity = new ChatModerator();
        when(chatModeratorService.findByChatId(any(Long.class))).thenReturn(Optional.of(entity));
        when(telegramService.sendMessage(any(SendMessage.class))).then(AnswerSendResponse.sendToConsole());
        Mockito.lenient().when(chatModeratorService.save(any(ChatModerator.class))).thenThrow(new IllegalStateException("Записи в БД не должно быть"));

        // then
        String commandText = BotCommand.ADD_MODER_CHAT.getText() + " 123"; // "/addModerChat 123"
        com.pengrad.telegrambot.model.Update update = getPrivateMessageUpdateWithText(commandText);

        assertTrue(adminMessageHandler.handle(update));
        assertNull(entity.getId());
    }

    @DisplayName("Тест добавления чата модераторов: добавление нового ID")
    @Test
    public void testAddModerChat_newId() {
        // when
        ChatModerator entity = new ChatModerator();
        when(chatModeratorService.findByChatId(any(Long.class))).thenReturn(Optional.empty());
        when(chatModeratorService.save(any(ChatModerator.class))).then(AnswerChatModerator.save(entity));

        // when
        String commandText = BotCommand.ADD_MODER_CHAT.getText() + " 123"; // "/addModerChat 123"
        com.pengrad.telegrambot.model.Update update = getPrivateMessageUpdateWithText(commandText);

        assertTrue(adminMessageHandler.handle(update));
        assertNotNull(entity.getId());
    }

    private Update getPrivateMessageUpdateWithText(String text) {
        return Update.builder()
                .message(Message.builder()
                        .text(text)
                        .chat(Chat.builder().type(com.pengrad.telegrambot.model.Chat.Type.Private).build())
                        .build())
                .build();
    }
}
