package org.codewithoutus.tgbotusers.model.repository;

import org.codewithoutus.tgbotusers.model.entity.UserJoining;
import org.codewithoutus.tgbotusers.model.enums.CongratulateStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserJoiningRepository extends CrudRepository<UserJoining, Integer> {

    List<UserJoining> findDistinctByChatIdInOrderByChatIdAscNumberAsc(Collection<Long> chatIds);

    boolean existsByChatIdAndAnniversaryNumberAndStatus(Long chatId, Integer anniversaryNumber, CongratulateStatus congratulateStatus);

    boolean existsByChatIdAndUserId(Long chatId, Long userId);

    @Query("""
            select u from UserJoining u
            where (chatId, anniversaryNumber) not in (
                select distinct u.chatId, u.anniversaryNumber from UserJoining u
                where u.status = ?2)
            and u.chatId in ?1
            order by u.chatId, u.number, u.joinTime""")
    List<UserJoining> findByChatIdAndNotStatus(List<Long> chatIds, CongratulateStatus status);
}
