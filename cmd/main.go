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

//
//type Inst struct {
//	logger   *logging.Logger
//	badword  *data.BadWord
//	jubiuser *data.JubUsers
//	moders   *data.ModerGroup
//	update   *tgbotapi.Update
//	bot      *tgbotapi.BotAPI
//	cfg      config.Config
//}
//
//func NewInst(cfg *config.Config, logger *logging.Logger, bot *tgbotapi.BotAPI) (*Inst, error) {
//
//	badWord, err := data.NewBadWordDB(cfg)
//	if err != nil {
//		logger.Fatalf("error connect bad word database: %v", err)
//	}
//	jubUser, err := data.NewJubUsersDB(cfg)
//	if err != nil {
//		logger.Fatalf("error connect jubilee user database: %v", err)
//	}
//	moders, err := data.NewModeratorsGroupDB(cfg)
//	if err != nil {
//		logger.Errorf("error connect moderators group database: %v", err)
//	}
//
//	return &Inst{
//		logger:   logger,
//		badword:  badWord,
//		jubiuser: jubUser,
//		moders:   moders,
//		bot:      bot,
//		cfg:      config.Config{},
//	}, nil
//}

func main() {

	log.Print("config initializing")
	cfg := config.GetConfig(cfgPath)

	log.Print("logger initializing")
	logger := logging.GetLogger(cfg.AppConfig.LogLevel)

	//BadWord, err := data.NewBadWordDB(cfg, logger)
	//if err != nil {
	//	logger.Fatalf("error connect bad word database: %v", err)
	//}
	//JubUser, err := data.NewJubUsersDB(cfg, logger)
	//if err != nil {
	//	logger.Fatalf("error connect jubilee user database: %v", err)
	//}
	//Moders, err := data.NewModeratorsGroupDB(cfg, logger)
	//if err != nil {
	//	logger.Errorf("error connect moderators group database: %v", err)
	//}

	modGroupId := cfg.ModersGroupID.ModeratorsGroup
	_, _, err := functions.AddModeratorsGroup(cfg.ModersGroupID.ModeratorsGroup, cfg)
	_, _, err = functions.AddModeratorsGroup(cfg.ModersGroupID.ModeratorsGroupGolang, cfg)
	_, _, err = functions.AddModeratorsGroup(cfg.ModersGroupID.ModeratorsGroupJava, cfg)
	_, _, err = functions.AddModeratorsGroup(cfg.ModersGroupID.ModeratorsGroupPython, cfg)
	_, _, err = functions.AddModeratorsGroup(cfg.ModersGroupID.ModeratorsGroup1S, cfg)
	_, _, err = functions.AddModeratorsGroup(cfg.ModersGroupID.ModeratorsGroupCSharp, cfg)
	if err != nil {
		logger.Info(err)
	}

	updChan, bot, err := telegram.StartBotByChan(cfg, logger)
	if err != nil {
		logger.Fatal(err)
	}

	//inst, err := data.NewInst(cfg, logger, bot)

	defer bot.StopReceivingUpdates()

	for {

		update := <-updChan

		if update.Message != nil {

			// text messages operations
			textmsg.WithTextQueryDo(update, bot, logger, modGroupId, cfg)

			// social messages from bot in chat
			socialmsg.WithSocialTextQueryDo(update, bot, logger)
		} else if update.CallbackQuery != nil {

			callbackmsg.WithCallBackDo(update, bot, logger, modGroupId, cfg)

		} else if update.Message.Command() != "" {

			//com menu (only moderator's chats)
			comandmsg.CommandQueryDo(update, bot, logger)
		} else if update.Message.NewChatMembers != nil {

			chatmembers.WithChatMembersDo(update, bot, logger, cfg)
		}

		// TODO inline help
		if update.InlineQuery != nil {
			log.Println("response from Inline query")
			query := update.InlineQuery.Query

			log.Println(query)

		}
	}
}
