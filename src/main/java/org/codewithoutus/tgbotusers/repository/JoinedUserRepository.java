package org.codewithoutus.tgbotusers.repository;

import org.codewithoutus.tgbotusers.model.JoinedUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JoinedUserRepository extends CrudRepository<JoinedUser, Integer> {
}
