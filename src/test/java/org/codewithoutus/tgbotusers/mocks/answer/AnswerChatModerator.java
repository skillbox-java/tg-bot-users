package org.codewithoutus.tgbotusers.mocks.answer;

import org.codewithoutus.tgbotusers.model.entity.ChatModerator;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.concurrent.atomic.AtomicInteger;

public class AnswerChatModerator {

    private static final AtomicInteger sequence = new AtomicInteger();

    public static Answer<ChatModerator> newEntity() {
        return new Answer<ChatModerator>() {
            @Override
            public ChatModerator answer(InvocationOnMock invocation) throws Throwable {
                ChatModerator entity = new ChatModerator();
                entity.setId(sequence.getAndIncrement());
                return entity;
            }
        };
    }

    public static Answer<ChatModerator> save(ChatModerator entity) {
        return new Answer<ChatModerator>() {
            @Override
            public ChatModerator answer(InvocationOnMock invocation) throws Throwable {
                entity.setId(sequence.getAndIncrement());
                return entity;
            }
        };
    }
}
