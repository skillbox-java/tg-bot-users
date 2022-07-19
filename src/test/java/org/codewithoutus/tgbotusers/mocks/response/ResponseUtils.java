package org.codewithoutus.tgbotusers.mocks.response;

import com.pengrad.telegrambot.response.SendResponse;
import lombok.SneakyThrows;

public class ResponseUtils {

    @SneakyThrows
    public static SendResponse newSendResponse() {
        return SendResponse.class.getConstructor().newInstance();
    }
}
