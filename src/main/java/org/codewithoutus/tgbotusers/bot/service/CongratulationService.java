package org.codewithoutus.tgbotusers.bot.service;

import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.GetChatMember;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.model.UserJoining;
import org.codewithoutus.tgbotusers.model.enums.CongratulateStatus;
import org.codewithoutus.tgbotusers.service.UserJoiningService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CongratulationService {

    private final UserJoiningService userJoiningService;
    
//    public void process(UserJoining userJoining, CongratulateStatus settableStatus) {
//
//        notificationService.removeKeyboardFromNotification();
//
//        if (settableStatus.equals(CongratulateStatus.CONGRATULATE)) {
//            Long chatId = userJoining.getChatId();
//            Long userId = userJoining.getUserId();
//            var getChatMember = new GetChatMember(chatId, userId);
//            var getChatMemberResponse = bot.execute(getChatMember);
//            User user = getChatMemberResponse.chatMember().user();
//            String userName = user.firstName();
//            Integer number = userJoining.getNumber();
//
//            notificationService.congratulateUser(chatId, userName, number);
//        }
//
//        userJoining.setStatus(settableStatus);
//        userJoiningService.save(userJoining);
//
//    }
}