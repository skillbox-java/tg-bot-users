package org.codewithoutus.tgbotusers.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.GetChatMember;
import com.pengrad.telegrambot.request.GetChatMemberCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotExecutorService {
    private final TelegramBot bot;
    
    
    public int getCount(long chatId) {
        var getChatMemberCount = new GetChatMemberCount(chatId);
        var getChatMemberCountResponse = bot.execute(getChatMemberCount);
        return getChatMemberCountResponse.count();
    }
    
    public User getUser(long chatId, long userId) {
        var getChatMember = new GetChatMember(chatId, userId);
        var getChatMemberResponse = bot.execute(getChatMember);
        return getChatMemberResponse.chatMember().user();
    }
}
