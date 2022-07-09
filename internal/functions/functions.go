package functions

import (
	"database/sql"
	"errors"
	"fmt"
	tgbotapi "github.com/go-telegram-bot-api/telegram-bot-api/v5"
	_ "github.com/mattn/go-sqlite3"
	"log"
	"path/filepath"
	"skbot/internal/config"
	"skbot/internal/data"
	"strings"
)

var (
	qb = "CREATE TABLE IF NOT EXISTS badwords (id INTEGER PRIMARY KEY, word VARCHAR (30) NOT NULL)"

	qbu = "SELECT * FROM badwords"

	mod = "CREATE TABLE IF NOT EXISTS moderators (id INTEGER PRIMARY KEY, groupid INTEGER NOT NULL)"

	modSel = "SELECT * FROM moderators"

	addNewUsersDB = "CREATE TABLE IF NOT EXISTS newJubileeUsers (id INTEGER PRIMARY KEY, serial INTEGER NOT NULL, " +
		"user_id INTEGER NOT NULL, user_name VARCHAR (30) NOT NULL, user_nick VARCHAR (50) DEFAULT ('нет ника'), " +
		"time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, group_name VARCHAR (50) NOT NULL)"
)

func TrimSymbolsFromSlice(s []string) (words []string, err error) {

	var messageUpd []string

	for _, k := range s {

		k = strings.Trim(k, "([]{}*).,!")
		messageUpd = append(messageUpd, k)
	}

	words = messageUpd

	return words, nil
}

func CheckBadWords(badList []string, cfg config.Config) (clearList []string, haveBadWords bool, err error) {

	var badWords []data.BadWords
	var badWord data.BadWords
	haveBadWords = false

	db, err := sql.Open("sqlite3", filepath.Join(cfg.DBFilePath+"badwords.db"))
	if err != nil {
		return
	}

	rows, err := db.Query(qbu)
	if err != nil {
		return nil, false, err
	}

	defer db.Close()

	for rows.Next() {
		err = rows.Scan(&badWord.ID, &badWord.Word)
		badWords = append(badWords, badWord)
	}

	for _, word := range badList {

		for _, bad := range badWords {

			if word == bad.Word {

				log.Println("найдено совпадение матерного слова в базе", word)
				haveBadWords = true
			}

		}

	}

	return clearList, haveBadWords, err

}

func AddBadWord(word string, cfg *config.Config) (bool, error) {

	var badWord data.BadWords
	var haveWord = false

	db, err := sql.Open("sqlite3", filepath.Join(cfg.DBFilePath, "badwords.db"))
	if err != nil {
		return false, err
	}

	stat, err := db.Prepare(qb)
	if err != nil {
		return false, err
	}

	_, _ = stat.Exec()

	rows, err := db.Query(qbu)
	if err != nil {
	}

	defer db.Close()

	for rows.Next() {
		err = rows.Scan(&badWord.ID, &badWord.Word)
		if badWord.Word == word {
			haveWord = true
			return true, nil
		}
	}

	if !haveWord {

		stat, err = db.Prepare(fmt.Sprintf("INSERT INTO badwords (word) VALUES ('%s')", word))
		stat.Exec()
		if err != nil {
			return false, errors.New("ошибка добавления матерного слова в базу")

		} else {
			return true, errors.New("новое матерное слово занесено в базу")
		}
	}

	return true, errors.New("added")

}

func AddModeratorsGroup(group int64, cfg *config.Config) (haveGroup bool, modGroups []data.ModeratorsGroup, err error) {

	var modGroup data.ModeratorsGroup
	haveGroup = false

	db, err := sql.Open("sqlite3", filepath.Join(cfg.DBFilePath, "moderators.db"))
	if err != nil {
		return haveGroup, nil, err
	}

	defer db.Close()

	stat, err := db.Prepare(mod)
	if err != nil {
		log.Println(err)
	}

	_, err = stat.Exec()
	if err != nil {
		return haveGroup, nil, err
	}

	rows, err := db.Query(modSel)
	if err != nil {
		log.Println(err)
	}

	for rows.Next() {
		err = rows.Scan(&modGroup.ID, &modGroup.GroupID)
		modGroups = append(modGroups, modGroup)
	}

	for _, grp := range modGroups {
		if grp.GroupID == group {
			haveGroup = true
			return haveGroup, modGroups, errors.New("have group")
		}
	}

	if !haveGroup && group != 0 {
		stat, err = db.Prepare(fmt.Sprintf("INSERT INTO moderators (groupid) VALUES ('%d')", group))
		if err != nil {
			log.Println(err)
		}
		_, _ = stat.Exec()
		haveGroup = true
	}
	return haveGroup, modGroups, nil
}

func GetModeratorsGroup(cfg *config.Config) (groups []data.ModeratorsGroup, err error) {

	var group data.ModeratorsGroup

	db, err := sql.Open("sqlite3", filepath.Join(cfg.DBFilePath, "moderators.db"))
	if err != nil {
		return nil, err
	}

	defer db.Close()

	rows, err := db.Query(modSel)
	if err != nil {
		log.Println(err)
	}

	for rows.Next() {
		err = rows.Scan(&group.ID, &group.GroupID)
		groups = append(groups, group)
	}

	return

}

func AddNewJubileeUser(newUser *tgbotapi.User, serial int, groupName string, cfg *config.Config) error {

	db, err := sql.Open("sqlite3", filepath.Join(cfg.DBFilePath, "newJubileeUsers.db"))
	if err != nil {
		return err
	}
	defer db.Close()

	log.Println("создание таблицы")
	stat, err := db.Prepare(addNewUsersDB)
	if err != nil {
		return err
	}
	_, _ = stat.Exec()

	stat, err = db.Prepare(fmt.Sprintf("INSERT INTO newJubileeUsers (serial, user_id, user_name, user_nick, "+
		"group_name) VALUES ('%d', '%d', '%s', '%s', '%s')", serial, newUser.ID, newUser.FirstName, newUser.UserName, groupName))
	if err != nil {
		log.Println(err)
	}
	_, err = stat.Exec()
	if err != nil {
		log.Println(err)
	}

	return nil
}

func GetJubileeUsers() (jubUsers []data.JubileeUser, err error) {

	var user data.JubileeUser
	var users []data.JubileeUser

	db, err := sql.Open("sqlite3", "./tg-bot-users/internal/sqlitedb/newJubileeUsers.db")
	if err != nil {
		return nil, err
	}
	defer db.Close()

	rows, err := db.Query("SELECT * FROM newJubileeUsers")
	if err != nil {
		return nil, err
	}

	for rows.Next() {
		err = rows.Scan(&user.ID, &user.Serial, &user.UserID, &user.UserName, &user.UserNick, &user.Time, &user.GroupName)
		users = append(users, user)
	}

	for _, v := range users {
		if v.Serial%500 == 0 || v.Serial%500 == 1 || v.Serial%500 == 2 {
			jubUsers = append(jubUsers, v)
		}
	}

	return jubUsers, nil

}
