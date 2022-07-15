package config

import (
	"github.com/ilyakaznacheev/cleanenv"
	"log"
	"sync"
)

type Config struct {
	Telegram struct {
		Token string `yaml:"tg_token" env:"TG-BOT-TOKEN" env-required:"true"`
		Sert  string `yaml:"tg_sert"`
	}
	AppConfig     AppConfig     `yaml:"app"`
	ModersGroupID ModersGroupID `yaml:"moderators"`
	MsgText       MsgText       `yaml:"msg_text"`
	DBFilePath    string        `yaml:"db_file_path" env:"DB-FILE-PATH" env-required:"true"`
	Multiplicity  int           `yaml:"multiplicity" env:"multiplicity" env-required:"true"`
}

type AppConfig struct {
	LogLevel string `yaml:"log_level" env:"TG-BOT-LogLevel" env-default:"error" env-required:"true"`
}

type ModersGroupID struct {
	ModeratorsGroup int64 `yaml:"moderators_group" env:"moderators_group" env-required:"true"`
}

type MsgText struct {
	MsgOfBadWordToUserChat string `yaml:"msg_of_bad_word_to_user_chat" env:"MSG-OF-BAD-WORDS-TO-USER-CHAT" env-required:"true"`
	MsgToChatIfNewUser     string `yaml:"msg_to_chat_if_new_user" env:"MSG-TO-CHAT-IF-NEW-USER" env-required:"true"`
	MsgModeratorMember     string `yaml:"msg_moderator_member" env:"msg_moderator_member"`
	MsgTrimSymbol          string `yaml:"msg_trim_symbol"`
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
