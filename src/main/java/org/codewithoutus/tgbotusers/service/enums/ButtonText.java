package org.codewithoutus.tgbotusers.service.enums;

import lombok.Getter;

@Getter
public enum ButtonText {

    CONGRATULATE ("Поздравить \uD83E\uDD73"),
    DECLINE ("Отклонить 🚫");
    
    private final String solution;
    
    ButtonText(String solution) {
        this.solution = solution;
    }
    
}
