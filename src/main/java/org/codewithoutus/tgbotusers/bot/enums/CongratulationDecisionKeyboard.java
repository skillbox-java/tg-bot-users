package org.codewithoutus.tgbotusers.bot.enums;

import com.google.gson.JsonObject;
import lombok.Getter;
import org.codewithoutus.tgbotusers.model.UserJoining;
import org.codewithoutus.tgbotusers.model.enums.CongratulateStatus;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Getter
public enum CongratulationDecisionKeyboard {

    CONGRATULATE("/congratulate", "Поздравить \uD83E\uDD73", CongratulateStatus.CONGRATULATE),
    DECLINE("/decline", "Отклонить 🚫", CongratulateStatus.DECLINE);

    private final String command;
    private final String representation;
    private final CongratulateStatus status;

    CongratulationDecisionKeyboard(String command, String representation, CongratulateStatus status) {
        this.command = command;
        this.representation = representation;
        this.status = status;
    }

    public String buildJsonKeyData(UserJoining userJoining, Long moderatorChatId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("moderatorChatId", moderatorChatId);
        jsonObject.addProperty("userId", userJoining.getUserId());
        jsonObject.addProperty("chatId", userJoining.getChatId());
        jsonObject.addProperty("command", command);
        return jsonObject.getAsString();
    }

    public static Optional<CongratulationDecisionKeyboard> defineKey(Map<String, String> callbackQueryData) {
        String command = callbackQueryData.get("command");
        return Arrays.stream(CongratulationDecisionKeyboard.values())
                .filter(value -> value.getCommand().equals(command))
                .findFirst();
    }
}