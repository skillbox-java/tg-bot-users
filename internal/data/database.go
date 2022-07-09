package data

import (
	"database/sql"
	"path/filepath"
	"skbot/internal/config"
	"skbot/pkg/logging"
)

type BadWord struct {
	logger  *logging.Logger
	wordsdb *sql.DB
}

type DataBase interface {
}

func NewBadWordDB(cfg *config.Config, logger *logging.Logger) (DataBase, error) {
	word, err := sql.Open("sqlite3", filepath.Join(cfg.DBFilePath, "badwords.db"))
	if err != nil {
		return nil, err
	}

	return &BadWord{
		logger:  logger,
		wordsdb: word,
	}, nil
}

func NewModeratorsGroupDB(cfg *config.Config, logger *logging.Logger) (DataBase, error) {
	return nil, nil
}

func NewJubUsersDB(cfg *config.Config, logger *logging.Logger) (DataBase, error) {
	return nil, nil
}
