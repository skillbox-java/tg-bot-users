package data

import (
	"time"
)

type BadWords struct {
	ID   int
	Word string
}
type ModeratorsGroup struct {
	ID              int
	ModerGroupID    int64
	ModerGroupTitle string
	UserGroupID     int64
	UserGroupTitle  string
}

type JubileeUser struct {
	ID        int
	Serial    int
	UserID    int
	UserName  string
	UserNick  string
	Time      time.Time
	GroupName string
	GroupID   int64
}
