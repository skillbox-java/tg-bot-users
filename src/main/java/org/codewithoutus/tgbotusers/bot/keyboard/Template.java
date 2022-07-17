package org.codewithoutus.tgbotusers.bot.keyboard;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.bot.enums.TemplatePattern;
import org.codewithoutus.tgbotusers.bot.service.TelegramService;
import org.codewithoutus.tgbotusers.config.NotificationTemplates;
import org.codewithoutus.tgbotusers.model.entity.UserJoining;
import org.codewithoutus.tgbotusers.model.enums.CongratulateStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

// TODO: Pavel - подумать куда определить этот класс или перенести функционал по кдругим классам
@Component
@RequiredArgsConstructor
public class Template {
    private static final String CROWN = "👑👑👑";
    private static final String SUB_CROWN = "🎉";
    private static final String NO_NICK = "ника нет";
    
    private final NotificationTemplates notificationTemplates;
    private final TelegramService telegramService;
    
    
    public String getCongratulateText(UserJoining userJoining) {
        String congratulationTemplate = notificationTemplates.getJoinCongratulation();
        return prepareDataForCongratulate(congratulationTemplate, userJoining);
    }
    
    public String getAlertText(UserJoining userJoining) {
        String joinAlertTemplate = notificationTemplates.getJoinAlert();
        return prepareDataForAlertAndUserInfo(joinAlertTemplate, userJoining);
    }
    
    public String getUserInfoText(UserJoining userJoining) {
        String userInfoTemplate = notificationTemplates.getJoinUserInfo();
    
        boolean congratulateStatus = userJoining.getStatus().equals(CongratulateStatus.CONGRATULATE);
        
        return prepareDataForAlertAndUserInfo(userInfoTemplate, userJoining)
                .replaceFirst(SUB_CROWN, congratulateStatus ? CROWN : SUB_CROWN);
    }
    
    private String prepareDataForCongratulate(String configTemplate, UserJoining userJoining) {
    
        User user = telegramService.getUser(userJoining.getChatId(), userJoining.getUserId());
        String firstName = user.firstName();
        String lastName = user.lastName();
        String userName = firstName + (lastName == null ? "" : (" " + lastName));
    
        int joiningNumber = userJoining.getNumber();
    
    
        return getText(configTemplate, "", userName, "", joiningNumber, "");
    }
    
    private String prepareDataForAlertAndUserInfo(String configTemplate, UserJoining userJoining) {
        
        Chat chat = telegramService.getChat(userJoining.getChatId());
        String groupName = chat.title();
        
        User user = telegramService.getUser(userJoining.getChatId(), userJoining.getUserId());
        String firstName = user.firstName();
        String lastName = user.lastName();
        String userName = firstName + (lastName == null ? "" : (" " + lastName));
        
        String nickName = user.username();
        nickName = nickName == null ? NO_NICK : nickName;
        
        int joiningNumber = userJoining.getNumber();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(
                FormatStyle.SHORT, FormatStyle.SHORT);
        String joinTime = userJoining.getJoinTime().format(formatter);
    
        String crown = userJoining.getStatus().equals(CongratulateStatus.CONGRATULATE) ? CROWN : SUB_CROWN;
    
        return getText(configTemplate, groupName, userName, nickName, joiningNumber, joinTime);
    }
    
    @NotNull
    private String getText(String configTemplate, String groupName, String userName,
                           String nickName, int joiningNumber, String joinTime) {
    
        return configTemplate
                .replaceFirst(TemplatePattern.GROUP_NAME.getPattern(), groupName)
                .replaceFirst(TemplatePattern.USER_NAME.getPattern(), userName)
                .replaceFirst(TemplatePattern.NICK_NAME.getPattern(), nickName)
                .replaceFirst(TemplatePattern.JOINING_NUMBER.getPattern(), String.valueOf(joiningNumber))
                .replaceFirst(TemplatePattern.JOIN_TIME.getPattern(), joinTime);
    }
}
