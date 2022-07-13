package org.codewithoutus.tgbotusers.model.repository;

import org.codewithoutus.tgbotusers.model.entity.TelegramCallbackData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramCallbackDataRepository extends CrudRepository<TelegramCallbackData, Integer> {

    TelegramCallbackData findById(int id);
}