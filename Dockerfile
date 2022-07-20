
FROM golang:1.18.3 as builder
LABEL stage=tgbotbuilder
WORKDIR /src


COPY go.mod .
COPY go.sum .
RUN go mod download

COPY . .

RUN make build

FROM alpine:latest as certs
LABEL stage=tgbotbuilder
RUN apk --update add ca-certificates

FROM scratch
COPY --from=certs /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/ca-certificates.crt
COPY --from=builder /app /app
ADD folders.tar.gz /



ENTRYPOINT [ "/app" ]