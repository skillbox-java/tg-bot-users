package org.codewithoutus.tgbotusers.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.codewithoutus.tgbotusers.model.entity.ChatModerator;
import org.codewithoutus.tgbotusers.model.entity.ChatUser;
import org.codewithoutus.tgbotusers.model.service.ChatModeratorService;
import org.codewithoutus.tgbotusers.model.service.ChatUserService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "chats-settings")
@RequiredArgsConstructor
@Slf4j
public class ChatSettings {

    private final ChatModeratorService chatModeratorService;
    private final ChatUserService chatUserService;

    private Boolean rewriteDatabaseSettingsOnStartup;
    private Integer anniversaryNumbersDelta;
    private Set<Integer> anniversaryNumbers;

    @Getter(AccessLevel.NONE)
    private Map<Long, List<Long>> chatsSettingsData; // only used for loading from application-settings file

    public int getAnniversaryJoinNumber(long chatId, int joinNumber) {
        return anniversaryNumbers.stream()
                .filter(anniversaryNumber -> joinNumber >= anniversaryNumber)
                .filter(anniversaryNumber -> joinNumber <= anniversaryNumber + anniversaryNumbersDelta)
                .findFirst()
                .orElse(0);
    }

    @PostConstruct
    private void synchronizeDataBaseSettings() {
        if (rewriteDatabaseSettingsOnStartup || chatModeratorService.findAll().isEmpty()) {
            rewriteDataBaseSettings();
        }
    }

    @Transactional
    private void rewriteDataBaseSettings() {
        chatModeratorService.deleteAll();
        chatUserService.deleteAll();

        // TODO : Павел - необходимо:
        //  1. при запуске приложения проверять настройки в базе,
        //     если в базе пусто проверять конфигурацию и записывать в базу
        //  2. при каждом запуске запуске актуализировать названия юзерских чатов (поле name)
        //     (нужно для возможности получения списка юбилейных с параметром названия группы,
        //     пример команды: [/luckyList@UsersTgBot Java разработчик])
        
        for (Map.Entry<Long, List<Long>> moderatorData : chatsSettingsData.entrySet()) {
            ChatModerator chatModerator = new ChatModerator();
            chatModerator.setChatId(moderatorData.getKey());

            for (Long userData : moderatorData.getValue()) {
                ChatUser chatUser = chatUserService.findByChatId(userData);
                if (chatUser == null) {
                    chatUser = new ChatUser();
                    chatUser.setChatId(userData);
                    chatUser = chatUserService.save(chatUser);
                }
                chatUser.addChatModerator(chatModerator);
                chatModerator.addChatUser(chatUser);
                chatModerator = chatModeratorService.save(chatModerator);
            }
        }
        log.info("Group settings was written to DB");
    }
}