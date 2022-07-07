package org.codewithoutus.tgbotusers.services;

import lombok.RequiredArgsConstructor;
import org.codewithoutus.tgbotusers.model.JoinedUser;
import org.codewithoutus.tgbotusers.repository.JoinedUserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinedUserService {
    
    private final JoinedUserRepository joinedUserRepository;
    
    public void save(JoinedUser joinedUser) {
        joinedUserRepository.save(joinedUser);
    }
    
}
