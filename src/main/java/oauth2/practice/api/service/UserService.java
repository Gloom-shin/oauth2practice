package oauth2.practice.api.service;

import lombok.RequiredArgsConstructor;
import oauth2.practice.api.entity.user.User;
import oauth2.practice.api.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUser(String userId) {
        return userRepository.findByUserId(userId);
    }
}