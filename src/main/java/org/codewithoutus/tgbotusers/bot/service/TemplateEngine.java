package org.codewithoutus.tgbotusers.bot.service;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.config.NotificationTemplates;
import org.codewithoutus.tgbotusers.model.entity.UserJoining;
import org.codewithoutus.tgbotusers.model.enums.CongratulateStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TemplateEngine {

    private final NotificationTemplates notificationTemplates;
    private final TelegramService telegramService;

    public String buildFromTemplate(String template, UserJoining userJoining, boolean showCrown) {
        Chat chat = telegramService.getChat(userJoining.getChatId());
        User user = telegramService.getUser(userJoining.getChatId(), userJoining.getUserId());

        return buildFromTemplate(template)
                .withJoinDate(userJoining.getJoinTime())
                .withJoinNumber(userJoining.getAnniversaryNumber())
                .withUserName(user)
                .withUserNickname(user)
                .withChatName(chat)
                .withWinnerCrown(showCrown && userJoining.getStatus() == CongratulateStatus.CONGRATULATE)
                .done();
    }

    public Builder buildFromTemplate(String template) {
        return new Builder(template, notificationTemplates);
    }

    public static class Builder {
        private final StringBuilder result;
        private final NotificationTemplates temp;

        private Builder(String template, NotificationTemplates templates) {
            result = new StringBuilder(template);
            this.temp = templates;
        }

        private void replace(String target, String replacement) {
            for (int i = result.lastIndexOf(target); i > -1; i = result.lastIndexOf(target, i)) {
                result.replace(i, i + target.length(), replacement);
            }
        }

        public Builder withChatName(Chat chat) {
            replace(temp.getVariables().get("chat-name"), chat.title());
            return this;
        }

        public Builder withUserName(User user) {
            String name = user.firstName() + (user.lastName() == null ? "" : (" " + user.lastName()));
            replace(temp.getVariables().get("user-name"), name);
            return this;
        }

        public Builder withUserNickname(User user) {
            String nickname = user.username() == null ? temp.getPlugs().get("no-nick") : user.username();
            replace(temp.getVariables().get("user-nickname"), nickname);
            return this;
        }

        public Builder withJoinDate(LocalDateTime dateTime) {
            replace(temp.getVariables().get("join-date"), temp.getDateTimeFormatter().format(dateTime));
            return this;
        }

        public Builder withJoinNumber(int number) {
            replace(temp.getVariables().get("join-number"), String.valueOf(number));
            return this;
        }

        public Builder withWinnerCrown(boolean isWinner) {
            result.insert(0, isWinner ? temp.getPlugs().get("winner-crown") : "");
            return this;
        }

        public String done() {
            return result.toString();
        }
    }
}
