package org.codewithoutus.tgbotusers.bot.service;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.*;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codewithoutus.tgbotusers.bot.Bot;
import org.codewithoutus.tgbotusers.bot.exception.TelegramSendMessageException;
import org.codewithoutus.tgbotusers.config.AppStaticContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramService {

    private final Bot bot;

    private <T extends BaseRequest<T, R>, R extends BaseResponse> R sendRequest(BaseRequest<T, R> request) {
        R response = bot.execute(request);
        if (!response.isOk()) {
            String requestAsString;
            try {
                requestAsString = AppStaticContext.OBJECT_MAPPER.createObjectNode()
                        .put("parameters", request.getParameters().toString())
                        .toString();

            } catch (Exception ex) {
                requestAsString = request.toString();
            }
            log.error("Request {} -- get error response {}", requestAsString, response);
            throw new TelegramSendMessageException(requestAsString);
        }
        return response;
    }

    public SendResponse sendMessage(SendMessage message) {
        SendResponse response = sendRequest(message);
        log.debug("Sent message text=''{}''. Status={}", message.getParameters().get("text"), response.isOk());
        return response;
    }

    public BaseResponse removeKeyboardFromMessage(long chatId, int messageId) {
        BaseResponse response = sendRequest(new EditMessageReplyMarkup(chatId, messageId).replyMarkup(new InlineKeyboardMarkup()));
        log.debug("Keyboard removed from message(id={}) in chat(id={}). Status={}", messageId, chatId, response.isOk());
        return response;
    }

    public User getUser(long chatId, long userId) {
        User user = sendRequest(new GetChatMember(chatId, userId)).chatMember().user();
        log.debug("Receive getUser: user(id={}, chatId={})", userId, chatId);
        return user;
    }

    public Chat getChat(long chatId) {
        Chat chat = sendRequest(new GetChat(chatId)).chat();
        log.debug("Receive getChat: chat(id={})", chatId);
        return chat;
    }

    public int getChatMembersCount(long chatId) {
        int count = sendRequest(new GetChatMemberCount(chatId)).count();
        log.debug("Receive getChatMembersCount: count={} for chat(id={})", count, chatId);
        return count;
    }
}