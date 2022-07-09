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
	YouTube struct {
		APIURL      string `yaml:"api_url"`
		AccessToken string `yaml:"access_token"`
	}
	AppConfig       AppConfig       `yaml:"app"`
	ChatCountConfig ChatCountConfig `yaml:"chat_count_config"`
	ModersGroupID   ModersGroupID   `yaml:"moderators"`
	DBFilePath      string          `yaml:"db_file_path" env:"DB-FILE-PATH" env-required:"true"`
}

type AppConfig struct {
	LogLevel string `yaml:"log_level" env:"TG-BOT-LogLevel" env-default:"error" env-required:"true"`
}

type ChatCountConfig struct {
	TestChatIdCount int64 `yaml:"test_chat_id_count"`
	ChatIdCount     int64 `yaml:"chat_id_count"`
}

type ModersGroupID struct {
	ModeratorsGroup       int64 `yaml:"moderators_group" env:"moderators_group" env-required:"true"`
	ModeratorsGroupGolang int64 `yaml:"moderators_group_golang" env:"moderators_group_golang"`
	ModeratorsGroupJava   int64 `yaml:"moderators_group_java" env:"moderators_group_java"`
	ModeratorsGroupPython int64 `yaml:"moderators_group_python" env:"moderators_group_python"`
	ModeratorsGroup1S     int64 `yaml:"moderators_group_1s" env:"moderators_group_1s"`
	ModeratorsGroupCSharp int64 `yaml:"moderators_group_csharp" env:"moderators_group_csharp"`
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
