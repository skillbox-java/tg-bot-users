package org.codewithoutus.tgbotusers.model.repository;

import org.codewithoutus.tgbotusers.model.entity.UserJoining;
import org.codewithoutus.tgbotusers.model.enums.CongratulateStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserJoiningRepository extends CrudRepository<UserJoining, Integer> {

    List<UserJoining> findByChatId(Long chatId);

    boolean existsByChatIdAndAnniversaryNumberAndStatus(Long chatId, Integer anniversaryNumber, CongratulateStatus congratulateStatus);
}
