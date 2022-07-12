package org.codewithoutus.tgbotusers.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.GetChatMember;
import com.pengrad.telegrambot.request.GetChatMemberCount;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramService {

    private final Bot bot;

    public void sendMessage(Long chatId, String text) {
        bot.execute(new SendMessage(chatId, text));
    }

    public int getChatMembersCount(long chatId) {
        var getChatMemberCount = new GetChatMemberCount(chatId);
        var getChatMemberCountResponse = bot.execute(getChatMemberCount);
        return getChatMemberCountResponse.count();
    }

    public User getUserInfo(long chatId, long userId) {
        var getChatMember = new GetChatMember(chatId, userId);
        var getChatMemberResponse = bot.execute(getChatMember);
        return getChatMemberResponse.chatMember().user();
    }
}