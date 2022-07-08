FROM python:3.9-alpine3.16
COPY . /app
RUN pip install -r /app/requirements.txt
CMD [ "python", "/app/main.py" ]
