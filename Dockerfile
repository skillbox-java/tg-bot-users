# создаем образ
FROM python:3.9-alpine3.16

#копируем все файлы в директорию /app докер-контейнера
COPY . /app

# устанавливаем все необходимые зависимости для работы приложения
RUN pip install -r /app/requirements.txt

# запускаем приложение
CMD [ "python", "/app/main.py" ]
