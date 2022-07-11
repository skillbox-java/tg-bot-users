package org.codewithoutus.tgbotusers.keyboard;

import lombok.Getter;

@Getter
public enum CongratulationDecisionKeyboard {

    CONGRATULATE("Поздравить \uD83E\uDD73"),
    DECLINE("Отклонить 🚫");

    private final String solution;

    CongratulationDecisionKeyboard(String solution) {
        this.solution = solution;
    }
}