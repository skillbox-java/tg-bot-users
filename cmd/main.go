package main

import (
	"flag"
	tgb "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	"log"
	"skbot/internal/callbackmsg"
	"skbot/internal/chatmembers"
	"skbot/internal/comandmsg"
	"skbot/internal/config"
	"skbot/internal/functions"
	"skbot/internal/textmsg"
	"skbot/pkg/client/telegram"
	"skbot/pkg/logging"
)

var cfgPath string

func init() {
	//flag.StringVar(&cfgPath, "config", "/tg-bot-users/conf.yml", "config file path")
	flag.StringVar(&cfgPath, "config", "/tg-bot-users/conf.yml", "config file path")
}

func main() {

	log.Print("config initializing")
	cfg := config.GetConfig(cfgPath)

	log.Print("logger initializing")
	logger := logging.GetLogger(cfg.AppConfig.LogLevel)

	db, err := functions.NewFuncList(cfg, logger)
	if err != nil {
		logger.Fatal(err)
	}

	err = db.NewData()
	if err != nil {
		logger.Error(err)
	}

	updChan, bot, err := telegram.StartBotByChan(cfg, logger)
	if err != nil {
		logger.Fatal(err)
	}

	moderGroup, err := db.GetModeratorsGroup()
	if err != nil {
		logger.Error(err)
	}

	if len(moderGroup) == 0 {

		groupInfo, _ := bot.Send(tgb.NewMessage(cfg.ModersGroupID.ModeratorsGroup, "test"))
		_, _, err = db.AddModeratorsGroup(cfg.ModersGroupID.ModeratorsGroup, groupInfo.Chat.Title)
		if err != nil {
			logger.Info(err)
		}
		_, _ = bot.Send(tgb.NewDeleteMessage(groupInfo.Chat.ID, groupInfo.MessageID))

	}

	defer bot.StopReceivingUpdates()

	for {

		update := <-updChan

		if update.Message != nil {

			if update.Message.Text != "" {
				// text messages operations
				textmsg.WithTextQueryDo(update, bot, logger, cfg)

			} else if update.Message.Command() != "" {

				//com menu (only moderator's chats)
				comandmsg.CommandQueryDo(update, bot, logger, cfg)

			} else if update.Message.NewChatMembers != nil {

				chatmembers.WithChatMembersDo(update, bot, logger, cfg)
			}

		} else if update.CallbackQuery != nil {

			callbackmsg.WithCallBackDo(update, bot, logger, cfg)
			// TODO inline help

		} else if update.InlineQuery != nil {

			log.Println("response from Inline query")
			query := update.InlineQuery.Query

			log.Println(query)

		}

	}
}
