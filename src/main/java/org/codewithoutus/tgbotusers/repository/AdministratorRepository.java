package org.codewithoutus.tgbotusers.repository;

import org.codewithoutus.tgbotusers.model.Administrator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends CrudRepository<Administrator, Integer> {
}
