package org.codewithoutus.tgbotusers.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RightsService {

//    private final TelegramBot bot;
//
//    public void initialiseAdministratorRights() {
//        if (!administratorRightsIsCorrect()) {
//            setAdministratorRights();
//        }
//    }
//
//    private boolean administratorRightsIsCorrect() {
//        var getRights = new GetMyDefaultAdministratorRights();
//        var response = bot.execute(getRights);
//        var rights = response.result();
//        return rights.canInviteUsers();
//    }
//
//    private void setAdministratorRights() {
//        var chatAdministratorRights = new ChatAdministratorRights();    // TODO add isAnonymous(boolean)
//        chatAdministratorRights.canInviteUsers(true);
//
//        var setMyDefaultAdministratorRights = new SetMyDefaultAdministratorRights();
//        setMyDefaultAdministratorRights.rights(chatAdministratorRights);
//        bot.execute(setMyDefaultAdministratorRights);
//    }
}