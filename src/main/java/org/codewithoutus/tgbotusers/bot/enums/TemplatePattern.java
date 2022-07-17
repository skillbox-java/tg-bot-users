package org.codewithoutus.tgbotusers.bot.enums;

public enum TemplatePattern {
    
    GROUP_NAME("НазваниеГруппы"),
    USER_NAME("ИмяУчастника"),
    NICK_NAME("НикУчастника"),
    JOINING_NUMBER("порядковыйНомерВступления"),
    JOIN_TIME("ВремяВступления");
    
    private final String pattern;
    
    TemplatePattern(String pattern) {
        this.pattern = pattern;
    }
    
    public String getPattern() {
        return '{' + pattern + '}';
    }
}
