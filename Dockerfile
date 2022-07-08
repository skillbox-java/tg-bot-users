FROM golang:1.18.3 as builder

WORKDIR /src

COPY go.mod .
COPY go.sum .
RUN go mod download

COPY . .

RUN make build

FROM scratch

COPY --from=builder /app /app

ENTRYPOINT [ "/app" ]
#EXPOSE 80