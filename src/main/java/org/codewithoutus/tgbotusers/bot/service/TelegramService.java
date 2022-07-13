package org.codewithoutus.tgbotusers.bot.service;

import com.pengrad.telegrambot.model.User;
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
        return sendRequest(message);
    }


    public void editMessageReplyMarkup(EditMessageReplyMarkup editMessageReplyMarkup) {
        sendRequest(editMessageReplyMarkup);
    }

    public int getChatMembersCount(long chatId) {
        return sendRequest(new GetChatMemberCount(chatId)).count();
    }

    public User getUser(long chatId, long userId) {
        return sendRequest(new GetChatMember(chatId, userId)).chatMember().user();
    }
}