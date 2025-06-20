package at.boot.user;

import at.boot.models.User;
import at.boot.repositories.UserRepository;
import at.boot.responses.UserProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserProfileDto getUserProfileByUsername(String username, Date issuedAt, Date expiration) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return UserProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .jwtissuedAtDate(issuedAt)
                .jwtexpirationDate(expiration)
                .build();
    }
}
