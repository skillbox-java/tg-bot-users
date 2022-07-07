package org.codewithoutus.tgbotusers.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.ChatJoinRequest;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.GetChatMember;
import com.pengrad.telegrambot.request.GetChatMemberCount;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.model.JoinedUser;
import org.codewithoutus.tgbotusers.services.JoinedUserService;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ChatJoinRequestHandler implements Handler {
    private static final int MULTIPLICITY = 500;
    private static final int INCREMENTAL_SAVES = 2;
    
    private final TelegramBot bot;
    private final JoinedUserService joinedUserService;
    
    @Override
    public void handle(Object handledObject) {
        ChatJoinRequest chatJoinRequest = (ChatJoinRequest) handledObject;
        
        long chatId = chatJoinRequest.chat().id();
        int count = getCount(chatId);
        if (count % MULTIPLICITY > INCREMENTAL_SAVES) {
            return;
        }
        
        long userId = chatJoinRequest.from().id();
        User user = getUser(chatId, userId);
        if (user.isBot()) {
            return;
        }
        
        int date = chatJoinRequest.date();
        
        JoinedUser joinedUser = createJoinedUser(user, chatId, count, date);
        joinedUserService.save(joinedUser);
        
        createNotification(user);
        sendNotification();
    }
    
    private int getCount(long chatId) {
        var getChatMemberCount = new GetChatMemberCount(chatId);
        var getChatMemberCountResponse = bot.execute(getChatMemberCount);
        return getChatMemberCountResponse.count();
    }
    
    private User getUser(long chatId, long userId) {
        var getChatMember = new GetChatMember(chatId, userId);
        var getChatMemberResponse = bot.execute(getChatMember);
        return getChatMemberResponse.chatMember().user();
    }
    
    private JoinedUser createJoinedUser(User user, long chatId, int count, int date) {
        JoinedUser joinedUser = new JoinedUser();
        joinedUser.setUserId(user.id());
        joinedUser.setChatId(chatId);
        joinedUser.setNumber(count);
        Instant time = Instant.ofEpochSecond(date);
        joinedUser.setJoinTime(LocalDateTime.from(time));
        return joinedUser;
    }
    
    private void createNotification(User user) {
    
    }
    
    private void sendNotification() {
    
    }
    
}
