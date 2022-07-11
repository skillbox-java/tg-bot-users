package org.codewithoutus.tgbotusers.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.exception.CommandNotFoundException;
import org.codewithoutus.tgbotusers.model.ModeratorChat;
import org.codewithoutus.tgbotusers.model.UserJoining;
import org.codewithoutus.tgbotusers.model.enums.CongratulateStatus;
import org.codewithoutus.tgbotusers.service.CallbackQueryService;
import org.codewithoutus.tgbotusers.service.CongratulationService;
import org.codewithoutus.tgbotusers.service.ModeratorChatService;
import org.codewithoutus.tgbotusers.service.UserChatService;
import org.codewithoutus.tgbotusers.service.UserJoiningService;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CallbackQueryHandler implements Handler {
    
    private final CallbackQueryService callbackQueryService;
    private final CongratulationService congratulationService;
    private final UserJoiningService userJoiningService;
    private final ModeratorChatService moderatorChatService;
    private final UserChatService userChatService;
    
    @Override
    public boolean handle(Update update) {
        
        CallbackQuery callbackQuery = update.callbackQuery();
        if (callbackQuery == null) {
            return false;
        }
        
        String data = callbackQuery.data();
        if (data == null || data.isBlank()) {
            return false;
        }
        
        Type mapType = new TypeToken<Map<String, String>>() {}.getType();
        Gson gson = new Gson().fromJson(data, mapType);
        Map<String, String> callbackQueryData = gson.fromJson(data, mapType);
        
        Long moderatorChatId = Long.valueOf(callbackQueryData.get("moderatorChatId"));
        Long chatId = Long.valueOf(callbackQueryData.get("chatId"));
        List<ModeratorChat> moderatorChats = userChatService.getUserChats()
                .get(chatId).getModeratorChats();
        boolean fromModeratorChat = moderatorChats.stream()
                .anyMatch(chat -> chat.getChatId().equals(moderatorChatId));
        if (!fromModeratorChat) {
            return false;
        }
        
        Long userId = Long.valueOf(callbackQueryData.get("userId"));
        UserJoining userJoining = userJoiningService.findByUserIdAndChatId(userId, chatId);
        String command = callbackQueryData.get("command");
        if (userJoining.getStatus().equals(CongratulateStatus.WAIT)) {
            if (command.equals("congratulate")) {
                callbackQueryService.process(userJoining, CongratulateStatus.CONGRATULATE);
                return true;
            } else if (command.equals("decline")) {
                callbackQueryService.process(userJoining, CongratulateStatus.DECLINE);
                return true;
            } else {
                throw new CommandNotFoundException("Wrong callback data. Callback data does not contain a command.");
            }
        }
        
        return false;
    }
}
