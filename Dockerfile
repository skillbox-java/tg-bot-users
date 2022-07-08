FROM golang:1.18.3 as builder

#RUN apk --no-cache --no-progress add tzdata make sqlite && rm -rf /var/cache/apk/*

WORKDIR /src

COPY go.mod .
COPY go.sum .
RUN go mod download

COPY . .

RUN make build

FROM scratch

#COPY --from=builder /usr/share/zoneinfo /use/share/zoneinfo
COPY --from=builder /app /app
#ADD config /config

ENTRYPOINT [ "/app" ]
#EXPOSE 80