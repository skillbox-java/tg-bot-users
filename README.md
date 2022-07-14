# tg-bot-users 🤖

Телеграм-бот отслеживает юбилейных пользователей групп. По умолчанию, юбилейными считаются участники, кратные 500. Эти настройки можно изменить при конфигурации бота.
При вступлении в комьюнити-группу нового, юбилейного участника бот присылает в группу модераторов уведомление с именем, ником, id пользователя, юбилейным номером и датой и временем вступления.
Помимо юбилейных, бот сохраняет двух последующих участников, так как юбилейным может вступить не пользователь, а бот или модератор.

## Особенности

* Команды боту доступны только в чатах модераторов.
* Настройки администрирования доступны только пользователям с правам администраторов.
* Чтобы поздравить юбилейного пользователя, нужно просто нажать кнопку Поздравить.

## Requirements

* Python 3.9+
* [pyTelegramBotAPI](https://github.com/python-telegram-bot/python-telegram-bot) – Python Telegram Bot API
* dotenv tbc

Если вы планируете запускать приложение локально, то можете установить все зависимости, выполнив следующую команду: `pip install -r requirements.txt`.

## Запуск приложения

### Подготовка к запуску
1. Если у вас еще нет telegram-бота, создайте его с помощью @BotFather и сохраните token от бота.
2. Добавьте вашего бота во все telegram-чаты, где вы хотите, чтобы он отслеживал юбилейных участников.

Приложение можно запустить локально либо через контейнер docker.

### Запустить приложение локально

1. Создать файл .env и наполнить его согласно инструкции из файла /.env.template.
2. Скачать приложение tg-bot-users в нужную директорию.
3. Сохранить файл .env в директорию приложения tg-bot-users.
4. В терминале перейти в директорию tg-bot-users.
5. Установить зависимости, выполнив в терминале следующую команду: `pip install -r requirements.txt`.
6. Запустить файл main.py

Теперь бот запущен локально и готов к работе!

### Запустить приложение через контейнер docker:

#### Вариант 1: через файл .env

1. Создать файл .env и наполнить его согласно инструкции из файла /.env.template.
2. Скачать приложение tg-bot-users в нужную директорию.
3. Сохранить файл .env в директории приложения tg-bot-users.
4. Установить и запустить docker (например, Docker Desktop).
5. В терминале перейти в директорию tg-bot-users.
6. Создать контейнер docker, введя в терминале команду:

		docker build -t telegram-bot:latest .

где `telegram-bot` - пример названия контейнера. Можно дать другое название контейнеру.

7. Запустить контейнер, введя в терминале команду:

		docker run --name telegram-bot --volume c:\Users\tg-bot-users\database:/app/database -d telegram-bot:latest

где `c:\Users\tg-bot-users\database` - пример абсолютного пути до директории database внутри директории tg-bot-users.
При работе на операционной системе Windows в пути стоит писать обратный слэш, как в примере. При работе с другими операционными системами в пути стоит писать прямой слэш `/`.
Эта команда позволит не терять данные из базы данных при перезапуске приложения и контейнера.

Теперь бот запущен и готов к работе!

#### Вариант 2: через передачу параметров при запуске docker контейнера

1. Скачать приложение tg-bot-users в нужную директорию.
2. Установить и запустить docker (например, Docker Desktop).
3. В терминале перейти в директорию tg-bot-users.
4. Создать контейнер docker, введя в терминале команду:

		docker build -t telegram-bot:latest .

где `telegram-bot` - пример названия контейнера. Можно дать другое название контейнеру.
5. Запустить контейнер, введя в терминале команду:

		docker run --name telegram-bot --volume c:\Users\Наташик\PycharmProjects\tg-bot-users\database:/app/database -e BOT_TOKEN=type_your_token -e ADMIN_IDS='["user_id_of_first_admin", "user_id_of_second_admin"]' telegram-bot:latest

где `type_your_token` - token от вашего бота;
`user_id_of_first_admin` и `user_id_of_second_admin` - user_id тех пользователей, которым вы хотите дать права администрирования (их не обязательно должно быть двое, может быть один, может быть больше);
`c:\Users\tg-bot-users\database` - пример абсолютного пути до директории database внутри директории tg-bot-users.
При работе на операционной системе Windows в пути стоит писать обратный слэш как в примере. При работе с другими операционными системами в пути стоит писать прямой слэш `/`.
Эта команда позволит не терять данные из базы данных при перезапуске приложения и контейнера.

Теперь бот запущен и готов к работе!

## Команды бота

* `/start` - запуск бота, выполняется автоматически при подключении к боту.
* `/help` - список команд и их описание
* `/luckylist` - по запросу названия группы присылать список всех зафиксированных пользователей за время работы


tbc по каждой команде + dotenv




