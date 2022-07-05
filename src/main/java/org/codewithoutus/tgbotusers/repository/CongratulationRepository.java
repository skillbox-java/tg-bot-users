package org.codewithoutus.tgbotusers.repository;

import org.codewithoutus.tgbotusers.model.Congratulation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CongratulationRepository extends CrudRepository<Congratulation, Integer> {
}
