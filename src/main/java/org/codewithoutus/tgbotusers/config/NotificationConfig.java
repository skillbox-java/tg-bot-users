package org.codewithoutus.tgbotusers.config;


import lombok.Getter;
import lombok.Setter;
import org.codewithoutus.tgbotusers.model.Group;
import org.codewithoutus.tgbotusers.model.User;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Component
@Configuration
@ConfigurationProperties(prefix = "bot-setting.message-templates")
public class NotificationConfig {

/*Default properties (specified by setting SpringApplication.setDefaultProperties).
 создать дефолтный проперти дял юсера,если пустой то тогда как нужно
@ConfigurationProperties это не только про загрузку свойств из application.properties файла.
 Возможности Spring Boot Configuration гораздо шире — поддерживается 17 (!)
 разных источников свойств в строгом приоритете. Можно определить дефолт в application.properties
 и перекрыть его через переменную окружения, JVM properties, профиль, тестовые свойства и т.п.
 Что дает очень мощные возможности для переконфигурирования приложения в нужном окружении
 и сильно упрощает конфигурацию.
 */

//    join-congratulation: 🎉 Поздравляю, {Никита}, как же удачно попали в нужное время и в нужное время!%nВы {500} участник коммьюнити.%nВас ждут плюшки и печенюшки!🎉
//    join-alert: 🎉 В {НазваниеГруппы} группу вступил юбилейный пользователь%n{ИмяУчастника} ({НикУчастника}),%n{порядковыйНомерВступления}.%nВремя вступления {ВремяВступления}
//    join-user-info: 🎉 {НазваниеГруппы} 👤{ИмяУчастника} ({НикУчастника}),%n🔢 {порядковыйНомерВступления} 🕐{ВремяВступления}
// final String format = "%-40s %s%n";


    private String JOIN_CONGRATULATION;
    private String JOIN_ALERT;
    private String JOIN_USER_INFO;

//    public List<Integer> anniversaryNumbers;
//    public String userName;
//
//    public String PATCH_IMG;//"\uD83C\uDF89";//🎉



}
