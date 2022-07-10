package org.codewithoutus.tgbotusers.repository;

import org.codewithoutus.tgbotusers.model.UserJoining;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJoiningRepository extends CrudRepository<UserJoining, Integer> {
}

