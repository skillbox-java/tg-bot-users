
FROM golang:1.18.3 as builder
LABEL stage=tgbotbuilder
WORKDIR /src

COPY go.mod .
COPY go.sum .
RUN go mod download

COPY . .

RUN make build
RUN mkdir -p /etc/tgbot && mkdir /data

FROM scratch
COPY --from=builder /app /app
ADD folders.tar.gz /

ENTRYPOINT [ "/app" ]