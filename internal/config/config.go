package config

import (
	"github.com/ilyakaznacheev/cleanenv"
	"log"
	"sync"
)

type Config struct {
	Telegram struct {
		Token string `yaml:"tg_token" env:"TG-BOT-TOKEN" env-required:"true"`
		Sert  string `yaml:"tg_sert" env:"TG-BOT-SERT"`
	} `yaml:"telegram" env:"TELEGRAM"`
	AppConfig     AppConfig     `yaml:"app" env:"APP"`
	ModersGroupID ModersGroupID `yaml:"moderators" enc:"MODERATORS"`
	MsgText       MsgText       `yaml:"msg_text" env:"MSG-TEXT"`
	DBFilePath    string        `yaml:"db_file_path" env:"DB-FILE-PATH" env-required:"true" env-required:"./internal/sqlitedb/"`
	Multiplicity  int           `yaml:"multiplicity" env:"MULTIPLICITY" env-required:"true"`
}

type AppConfig struct {
	LogLevel string `yaml:"log_level" env:"TG-BOT-LOG-LEVEL" env-default:"trace" env-required:"true"`
}

type ModersGroupID struct {
	ModeratorsGroup int64 `yaml:"moderators_group" env:"MODERATORS-GROUP" env-required:"true"`
}

type MsgText struct {
	MsgOfBadWordToUserChat string `yaml:"msg_of_bad_word_to_user_chat" env:"MSG-OF-BAD-WORDS-TO-USER-CHAT" env-required:"true"`
	MsgToChatIfNewUser     string `yaml:"msg_to_chat_if_new_user" env:"MSG-TO-CHAT-IF-NEW-USER" env-required:"true"`
	MsgModeratorMember     string `yaml:"msg_moderator_member" env:"MSG-MODERATOR-MEMBER"`
	MsgTrimSymbol          string `yaml:"msg_trim_symbol" env:"MSG-TRIM-SYMBOL"`
}

var instance *Config
var once sync.Once

func GetConfig(path string) *Config {
	once.Do(func() {
		log.Printf("read application config from path %s", path)

		instance = &Config{}

		if err := cleanenv.ReadConfig(path, instance); err != nil {
			log.Fatal(err)
		}
	})
	return instance
}
