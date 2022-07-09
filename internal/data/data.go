package data

import "time"

//type User struct {
//	ID   int
//	Name string
//	Sex  string
//}

type BadWords struct {
	ID   int
	Word string
}
type ModeratorsGroup struct {
	ID      int
	GroupID int64
}

type JubileeUser struct {
	ID        int
	Serial    int
	UserID    int
	UserName  string
	UserNick  string
	Time      time.Time
	GroupName string
}
