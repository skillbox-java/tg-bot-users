package telegram

import (
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	"log"
	"skbot/internal/config"
	"skbot/pkg/logging"
	"time"
)

func StartBotByChan(cfg *config.Config, logger *logging.Logger) (tgbotapi.UpdatesChannel, *tgbotapi.BotAPI, error) {

	bot, err := tgbotapi.NewBotAPI(cfg.Telegram.Token)
	if err != nil {
		log.Fatal(err)
	}

	bot.Debug = false

	logger.Infof("Bot %s started at: %v", bot.Self.UserName, time.Now().Format("2 January 2006 15:04"))

	u := tgbotapi.NewUpdate(0)
	u.Timeout = 60
	u.Limit = 0
	u.Offset = 0

	updates := bot.GetUpdatesChan(u)

	return updates, bot, nil

}
