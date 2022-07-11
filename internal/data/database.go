package data

import (
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	"skbot/internal/config"
	"skbot/pkg/logging"
)

type Inst struct {
	logger *logging.Logger
	update *tgbotapi.Update
	bot    *tgbotapi.BotAPI
	cfg    config.Config
}

func NewInst(cfg *config.Config, logger *logging.Logger, bot *tgbotapi.BotAPI) (*Inst, error) {

	return &Inst{
		logger: logger,
		bot:    bot,
		cfg:    config.Config{},
	}, nil
}
