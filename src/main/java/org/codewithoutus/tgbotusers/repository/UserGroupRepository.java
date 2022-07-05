package org.codewithoutus.tgbotusers.repository;

import org.codewithoutus.tgbotusers.model.UserGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends CrudRepository<UserGroup, Integer> {
}
