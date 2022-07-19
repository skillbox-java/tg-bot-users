FROM python:3.9-buster
RUN mkdir /bot_app

COPY . /bot_app
WORKDIR /bot_app

RUN pip install --upgrade pip
RUN pip install -r requirements.txt

CMD ["python", "main.py"]