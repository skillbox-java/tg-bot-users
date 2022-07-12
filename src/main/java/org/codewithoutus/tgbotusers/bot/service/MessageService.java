package org.codewithoutus.tgbotusers.bot.service;

import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.service.UserChatService;
import org.codewithoutus.tgbotusers.service.UserJoiningService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final UserChatService userChatService;
    private final UserJoiningService userJoiningService;
    
//    public void process(String text, MessageEntity entity) {
//
//        if (text.contains(BotCommand.LUCKY_LIST.getCommand())) {
//            performLuckyList(text, entity);
//        }
//
//
//    }
//
//    @Deprecated
//    private String getCommand(String text, MessageEntity entity) {
//        int begin = entity.offset();
//        int end = begin + entity.length();
//        String command = text.substring(begin, end);
//        if (command.contains("@")) {
//            return command.split("@")[0];
//        }
//        return command;
//    }
//
//    private void performLuckyList(String text, MessageEntity entity) {
//        String chatName = parseParam(text, entity);
//        UserChat userChat = userChatService.findByName(chatName);
//        Long userChatId = userChat.getChatId();
//        List<UserJoining> userJoinings = userJoiningService.findByChatId(userChatId);
//        userJoinings.sort(Comparator.naturalOrder());
//
//        for (UserJoining userJoining : userJoinings) {
//            // TODO implement logic
//        }
//
//    }
//
//    private String parseParam(String text, MessageEntity entity) {
//        String param = text.substring(entity.offset() + entity.length());
//        return param.trim();
//    }
}
