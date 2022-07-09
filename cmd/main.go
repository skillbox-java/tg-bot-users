package main

import (
	"flag"
	"log"
	"skbot/internal/callbackmsg"
	"skbot/internal/chatmembers"
	"skbot/internal/comandmsg"
	"skbot/internal/config"
	"skbot/internal/functions"
	"skbot/internal/socialmsg"
	"skbot/internal/textmsg"
	"skbot/pkg/client/telegram"
	"skbot/pkg/logging"
)

var cfgPath string

func init() {
	flag.StringVar(&cfgPath, "config", "tg-bot-users/conf.yml", "config file path")
}

func main() {

	log.Print("config initializing")
	cfg := config.GetConfig(cfgPath)

	log.Print("logger initializing")
	logger := logging.GetLogger(cfg.AppConfig.LogLevel)

	modGroupId := cfg.ModersGroupID.ModeratorsGroup
	_, _, err := functions.AddModeratorsGroup(cfg.ModersGroupID.ModeratorsGroup)
	_, _, err = functions.AddModeratorsGroup(cfg.ModersGroupID.ModeratorsGroupGolang)
	_, _, err = functions.AddModeratorsGroup(cfg.ModersGroupID.ModeratorsGroupJava)
	_, _, err = functions.AddModeratorsGroup(cfg.ModersGroupID.ModeratorsGroupPython)
	_, _, err = functions.AddModeratorsGroup(cfg.ModersGroupID.ModeratorsGroup1S)
	_, _, err = functions.AddModeratorsGroup(cfg.ModersGroupID.ModeratorsGroupCSharp)
	if err != nil {
		logger.Info(err)
	}

	updChan, bot, err := telegram.StartBotByChan(cfg, logger)
	if err != nil {
		logger.Fatal(err)
	}
	defer bot.StopReceivingUpdates()

	for {

		update := <-updChan

		if update.Message != nil {

			// text messages operations
			textmsg.WithTextQueryDo(update, bot, logger, modGroupId)

			// social messages from bot in chat
			socialmsg.WithSocialTextQueryDo(update, bot, logger)
		} else if update.CallbackQuery != nil {

			callbackmsg.WithCallBackDo(update, bot, logger, modGroupId)

		} else if update.Message.Command() != "" {

			//com menu (only moderator's chats)
			comandmsg.CommandQueryDo(update, bot, logger)
		} else if update.Message.NewChatMembers != nil {

			chatmembers.WithChatMembersDo(update, bot, logger)
		}

		// TODO inline help
		if update.InlineQuery != nil {
			log.Println("response from Inline query")
			query := update.InlineQuery.Query

			log.Println(query)

		}

	}

}
