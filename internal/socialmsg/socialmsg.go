package socialmsg

import (
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	"skbot/internal/functions"
	"skbot/pkg/logging"
	"strings"
)

func WithSocialTextQueryDo(update tgbotapi.Update, bot *tgbotapi.BotAPI, logger *logging.Logger) {

	// trim symbols and make slice from text message
	if len(update.Message.Text) > 0 {

		command, err := functions.TrimSymbolsFromSlice(strings.Split(update.Message.Text, " "))
		if err != nil {
			logger.Info("error trim symbols from message")
		}

		switch strings.ToLower(command[0]) {
		case "":

		default:

		}
	}
}
