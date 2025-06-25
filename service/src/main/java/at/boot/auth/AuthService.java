package at.boot.auth;

import at.boot.enums.Role;
import at.boot.jwt.JWTService;
import at.boot.models.User;
import at.boot.repositories.UserRepository;
import at.boot.requests.LoginRequest;
import at.boot.responses.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private BCryptPasswordEncoder encoder;


    public User register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        String generatedEmail = generateRandomEmail(user.getUsername());
        user.setEmail(generatedEmail);
        return userRepository.saveNew(user);
    }

    private String generateRandomEmail(String username) {
        String domain = "example.com";
        return username.toLowerCase() + "@" + domain;
    }
    public LoginResponse verify(LoginRequest loginRequest) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            if (authenticate.isAuthenticated()) {
                String token = jwtService.generateToken(loginRequest.getUsername());

                User userLogged = userRepository.findByUsername(loginRequest.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException("username or email not found"));

                return new LoginResponse(token, userLogged.getUsername(), userLogged.getRole());
            } else {
                throw new BadCredentialsException("Username or password is incorrect");
            }
        } catch (AuthenticationException ex) {
            // throw, damit GlobalExceptionHandler sie abfangen kann
            throw ex;
        }
    }


}
