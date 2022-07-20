FROM openjdk:17-alpine
COPY target/tg-bot-users-0.0.1-SNAPSHOT.jar tg-bot-users.jar
COPY application-bot.yml .
CMD java -jar tg-bot-users.jar