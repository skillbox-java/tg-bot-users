# tg-bot-users 🤖

---
## Общие сведения
Приложение является телеграм ботом


Основные функции:
  * Бот отслеживает вступление в группу новых пользователей.
  * В случае юбилейного вступления пользователя, присылает уведомление в группу модераторов для принятия решения о поздравлении.
    * При нажатии кнопки "Поздравить" приходит автоматическое поздравление вступившего пользователя.
    * При нажатии кнопки "Отклонить" поздравления не происходит.


Дополнительные функции:
  * Помимо самого юбилейного вступления, отслеживается несколько последующих - для ситуаций, если юбилейным оказался бот или модератор. Уведомление присылается на каждое подобное вступление, если решение о поздравлении ещё не было принято.
  * Модератор может запросить у бота список всех юбилейных вступлений. Если в юбилейном списке есть ожидающие поздравления, то модератор может принять решение, пользуясь полученным списком. При этом раннее отклоненные решения могут быть изменены. 


Менять настройки групп могут только администраторы.


---
## Команды бота

### Команды администраторов
_Доступны администраторам в приватном чате_

| Команда                            | Описание                                                |
|------------------------------------|---------------------------------------------------------|
| /help                              | вывод списка доступных команд                           |
| /addModerChat {id}                 | добавление чата модераторов                             |
| /addUserChat {id}                  | добавление чата пользователей                           |
| /deleteModerChat {id}              | удаление чата модераторов                               |
| /deleteUserChat {id}               | удаление чата пользователей                             |
| /bindUserChatToModer {id} {id}     | привязка чата пользователей к чату модераторов          |
| /unbindUserChatFromModer {id} {id} | удаление привязки чата пользователей к чату модераторов |


### Команды модераторов
_Доступны в чатах модераторов_

| Команда             | Описание                                                                        |
|---------------------|---------------------------------------------------------------------------------|
| /luckyList          | вывод списка юбилейных вступлений во всех привязанных чатах пользователей       |
| /luckyList {id}     | вывод списка юбилейных вступлений в конкретном чате                             |
| /chooseLucky        | вывод списка **ожидающих поздравления** во всех привязанных чатах пользователей |
| /chooseLucky {id}   | вывод списка **ожидающих поздравления** в конкретном чате                       |


---
## Web API
На всякий случай для бота сделан небольшой API.<br/>По умолчанию используется порт 8090.

| Endpoint         | Описание                                                                                        |
|------------------|-------------------------------------------------------------------------------------------------|
| /api/start       | Ручной старт бота                                                                               |
| /api/stop        | Ручная остановка бота                                                                           |
| /api/status      | Текущий статус бота                                                                             |
| /api/sendMessage | Отправка сообщения от бота<br/> `chatId` ID чата, куда отправить<br/> `message` текст сообщения |


---
## Настройки приложения

Конфигурационные файлы приложения разделены на две части:
### Настройки для разработчиков _(application.yml)_:
  * Spring
  * Database _(environment vars)_
  * Bot token _(environment vars)_


### Настройки функций бота _(application-bot.yml)_:
Настройки групп в приоритете берутся из базы данных. Из файла конфигурации эти настройки подтягиваются только в случае отсутствия таковых в БД, либо если включен флаг перезаписи настроек.

  * Настройки для чатов `chats-settings`:
    * `administrators` - список ID администраторов
    * `anniversary-numbers` - список юбилейных номеров
    * `anniversary-numbers-delta` - количество дополнительно отслеживаемых вступлений
    * `chats-settings` - настройки групп. Настройки прописываются иерархически: к каждому ID группы модераторов прописывается список ID групп пользователей.
    * `rewrite-chats-settings-in-database-on-startup` - перезапись настроек групп в базе данных на настройки из конфигурационного файла.


  * Настройки шаблонов `message-templates`:
    * `variables` - названия переменных шаблонов
    * `plugs` - дополнительные моменты
    * `join-congratulation` - шаблон сообщения поздравления пользователя
    * `join-alert` - шаблон уведомления модераторов о юбилейном вступлении пользователя
    * `join-user-info` - шаблон данных пользователя при использовании команды `/luckyList`


---
## Примеры сообщений

Уведомление модераторов

    🎉 “Java разработчик” 👤 Василий (ника нет),
    🔢 500 🕐 26.06.22 10:56
    [ПОЗДРАВИТЬ] [ОТКЛОНИТЬ]

Поздравление пользователя

    🎉 Поздравляю, Никита,
    как же удачно попали в нужное время и в нужное время!
    Вы 500 участник коммьюнити.
    Вас ждут плюшки и печенюшки!🎉
