package oauth2.practice.oauth.service;

import lombok.RequiredArgsConstructor;
import oauth2.practice.api.entity.user.User;
import oauth2.practice.api.repository.UserRepository;
import oauth2.practice.oauth.entity.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return UserPrincipal.create(user);
    }
}
