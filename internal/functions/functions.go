package functions

import (
	"database/sql"
	"errors"
	"fmt"
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	_ "github.com/mattn/go-sqlite3"
	"log"
	"path"
	"skbot/internal/config"
	"skbot/internal/data"
	"skbot/pkg/logging"
	"strings"
	"time"
)

type list struct {
	cfg    *config.Config
	logger *logging.Logger
	db     *sql.DB
}

func NewFuncList(cfg *config.Config, logger *logging.Logger) (FuncList, error) {

	liteDb, err := sql.Open("sqlite3", path.Join(cfg.DBFilePath, "skb_bot_db.db"))
	if err != nil {
		logger.Fatalf("error open database %v", err)
	}

	return &list{
		cfg:    cfg,
		logger: logger,
		db:     liteDb,
	}, nil

}

func (l *list) NewData() error {

	stat, err := l.db.Prepare("CREATE TABLE IF NOT EXISTS moderators " +
		"(id INTEGER PRIMARY KEY, moder_group_id INTEGER NOT NULL, moder_group_title TEXT DEFAULT no_title, " +
		"user_group_id INTEGER DEFAULT 0, user_group_title TEXT DEFAULT no_title)")
	_, err = stat.Exec()
	if err != nil {
		return err
	}

	stat, err = l.db.Prepare("CREATE TABLE IF NOT EXISTS bad_words " +
		"(id INTEGER PRIMARY KEY, word VARCHAR (30) NOT NULL)")
	_, err = stat.Exec()
	if err != nil {
		return err
	}

	stat, err = l.db.Prepare("CREATE TABLE IF NOT EXISTS newJubileeUsers " +
		"(id INTEGER PRIMARY KEY, serial INTEGER NOT NULL, " +
		"user_id INTEGER NOT NULL, user_name VARCHAR (30) NOT NULL, " +
		"user_nick VARCHAR (50) DEFAULT ('нет ника'), time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
		"group_name VARCHAR (50) NOT NULL, group_id INTEGER NOT NULL)")
	_, err = stat.Exec()
	if err != nil {
		return err
	}
	return nil
}

type FuncList interface {
	NewData() error
	AddUserGroupList(moderGroup, userGroup int64, moderTitle, userTitle string) (bool, error)
	CheckBadWords(badList []string) (clearList []string, haveBadWords bool, err error)
	AddBadWord(word string) (bool, error)
	AddModeratorsGroup(group int64, title string) (haveGroup bool, modGroups []data.ModeratorsGroup, err error)
	GetModeratorsGroup() (groups []data.ModeratorsGroup, err error)
	AddNewJubileeUser(newUser *tgbotapi.User, serial int, update tgbotapi.Update) error
	GetJubileeUsers() (jubUsers []data.JubileeUser, err error)
	GetAllJubileeUsers() (jubUsers []data.JubileeUser, err error)
}

func TrimSymbolsFromSlice(s []string) (words []string, err error) {

	var messageUpd []string

	for _, k := range s {

		k = strings.Trim(k, "([]{}*).,!")
		messageUpd = append(messageUpd, k)
	}

	words = messageUpd

	return words, nil
}

func (l *list) CheckBadWords(badList []string) (clearList []string, haveBadWords bool, err error) {

	var badWords []data.BadWords
	var badWord data.BadWords
	haveBadWords = false

	rows, err := l.db.Query("SELECT * FROM bad_words")
	if err != nil {
		return nil, false, err
	}

	for rows.Next() {
		err = rows.Scan(&badWord.ID, &badWord.Word)
		badWords = append(badWords, badWord)
	}

	for _, word := range badList {

		for _, bad := range badWords {

			if word == bad.Word {

				l.logger.Infof("найдено совпадение матерного слова в базе: %s", word)
				haveBadWords = true
			}
		}
	}

	return clearList, haveBadWords, err

}

func (l *list) AddBadWord(word string) (bool, error) {

	var badWord data.BadWords
	var haveWord = false

	rows, err := l.db.Query("SELECT * FROM bad_words")
	if err != nil {
	}

	for rows.Next() {
		err = rows.Scan(&badWord.ID, &badWord.Word)
		if badWord.Word == word {
			haveWord = true
			return true, nil
		}
	}

	if !haveWord {

		_, err = l.db.Exec(fmt.Sprintf("INSERT INTO bad_words (word) VALUES ('%s')", word))
		if err != nil {
			return false, errors.New("ошибка добавления матерного слова в базу")

		} else {
			return true, errors.New("новое матерное слово занесено в базу")
		}
	}

	return true, errors.New("added")

}

func (l *list) AddModeratorsGroup(group int64, title string) (haveGroup bool, modGroups []data.ModeratorsGroup, err error) {

	var modGroup data.ModeratorsGroup
	haveGroup = false

	rows, err := l.db.Query("SELECT * FROM moderators")
	if err != nil {
		log.Println(err)
	}

	for rows.Next() {
		err = rows.Scan(&modGroup.ID, &modGroup.ModerGroupID, &modGroup.ModerGroupTitle, &modGroup.UserGroupID, &modGroup.UserGroupTitle)
		modGroups = append(modGroups, modGroup)
	}

	for _, grp := range modGroups {
		if grp.ModerGroupID == group {
			haveGroup = true
			return haveGroup, modGroups, errors.New("have group")
		}
	}

	if !haveGroup && group != 0 {
		_, err = l.db.Exec(fmt.Sprintf("INSERT INTO moderators (moder_group_id, moder_group_title, user_group_id , "+
			"user_group_title) VALUES ('%d', '%s', '0', 'Без пользователей.')", group, title))
		if err != nil {
			log.Println(err)
		}

		haveGroup = true
	}

	return haveGroup, modGroups, nil
}

func (l *list) GetModeratorsGroup() (groups []data.ModeratorsGroup, err error) {

	rows, err := l.db.Query("SELECT * FROM moderators")
	if err != nil {
		log.Println(err)
	}

	var group data.ModeratorsGroup
	for rows.Next() {
		err = rows.Scan(&group.ID, &group.ModerGroupID, &group.ModerGroupTitle, &group.UserGroupID, &group.UserGroupTitle)
		groups = append(groups, group)
	}

	return groups, nil

}

func (l *list) AddNewJubileeUser(newUser *tgbotapi.User, serial int, update tgbotapi.Update) error {

	t := time.Now().Local().Format(config.StructDateTimeFormat)
	
	_, err := l.db.Exec(fmt.Sprintf("INSERT INTO newJubileeUsers (serial, user_id, user_name, user_nick, time, "+
		"group_name, group_id) VALUES ('%d', '%d', '%s', '%s', '%s', '%s', '%d')", serial, newUser.ID, newUser.FirstName,
		newUser.UserName, t, update.Message.Chat.Title, update.Message.Chat.ID))

	if err != nil {
		l.logger.Error(err)
	}

	return nil
}

func (l *list) GetJubileeUsers() (jubUsers []data.JubileeUser, err error) {

	var user data.JubileeUser
	var users []data.JubileeUser

	//TODO limit 3 last users
	rows, err := l.db.Query("SELECT * FROM newJubileeUsers ORDER BY id DESC LIMIT 4 ")
	if err != nil {
		return nil, err
	}

	for rows.Next() {
		err = rows.Scan(&user.ID, &user.Serial, &user.UserID, &user.UserName, &user.UserNick,
			&user.Time, &user.GroupName, &user.GroupID)
		users = append(users, user)
	}
	//TODO FIX serial 3
	for _, v := range users {

		if v.Serial%l.cfg.Multiplicity == 0 || v.Serial%l.cfg.Multiplicity == 1 || v.Serial%l.cfg.Multiplicity == 2 || v.Serial%3 == 0 {
			jubUsers = append(jubUsers, v)
		}
	}

	return jubUsers, nil

}

func (l *list) GetAllJubileeUsers() (jubUsers []data.JubileeUser, err error) {

	var user data.JubileeUser
	var users []data.JubileeUser

	rows, err := l.db.Query("SELECT * FROM newJubileeUsers")
	if err != nil {
		return nil, err
	}

	for rows.Next() {
		err = rows.Scan(&user.ID, &user.Serial, &user.UserID, &user.UserName, &user.UserNick,
			&user.Time, &user.GroupName, &user.GroupID)
		users = append(users, user)
	}

	return users, nil

}

func (l *list) AddUserGroupList(moderGroup, userGroup int64, moderTitle, userTitle string) (bool, error) {

	var moderatorGroup data.ModeratorsGroup
	var moderatorGroups []data.ModeratorsGroup
	var haveGroup = false

	l.logger.Infof("moder from func %d", moderGroup)

	rows, err := l.db.Query("SELECT * FROM moderators")
	if err != nil {
		return false, err
	}

	for rows.Next() {
		err := rows.Scan(&moderatorGroup.ID, &moderatorGroup.ModerGroupID, &moderatorGroup.ModerGroupTitle, &moderatorGroup.UserGroupID, &moderatorGroup.UserGroupTitle)
		if err != nil {
			return false, err
		}

		moderatorGroups = append(moderatorGroups, moderatorGroup)
	}

	for _, group := range moderatorGroups {

		if group.ModerGroupID == moderGroup && group.UserGroupID == userGroup {
			haveGroup = true
			return true, nil

		} else if group.ModerGroupID == moderGroup && group.UserGroupID == 0 {
			_, err = l.db.Exec(fmt.Sprintf("UPDATE moderators SET (user_group_id, user_group_title) = ('%d', '%s') "+
				"WHERE moder_group_id = ('%d')", userGroup, userTitle, moderGroup))
			if err != nil {
				return false, err
			}

			haveGroup = true
			return false, nil

		}
	}

	if !haveGroup {
		_, err = l.db.Exec(fmt.Sprintf("INSERT INTO moderators (moder_group_id, moder_group_title, user_group_id, user_group_title) "+
			"VALUES ('%d', '%s', '%d', '%s')", moderGroup, moderTitle, userGroup, userTitle))
		if err != nil {
			return false, err
		}
	}

	return false, nil

}
