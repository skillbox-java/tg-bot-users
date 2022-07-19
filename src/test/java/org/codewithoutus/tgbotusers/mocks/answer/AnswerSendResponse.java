package org.codewithoutus.tgbotusers.mocks.answer;

import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.codewithoutus.tgbotusers.mocks.response.ResponseUtils;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class AnswerSendResponse {

    public static Answer<SendResponse> sendToConsole() {
        return new Answer<SendResponse>() {
            @Override
            public SendResponse answer(InvocationOnMock invocationOnMock) throws Throwable {
                SendMessage sendMessage = (SendMessage) invocationOnMock.getArgument(0);
                System.out.println(sendMessage.getParameters().get("text"));
                return null;
            }
        };
    }
}
