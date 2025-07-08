package at.boot.auth;

import at.boot.enums.Role;
import at.boot.jwt.JWTService;
import at.boot.models.User;
import at.boot.repositories.UserRepository;
import at.boot.requests.LoginRequest;
import at.boot.responses.LoginResponse;
import at.boot.responses.RegisterUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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


    public User register(RegisterUserDTO dto) {
        User user = User.builder()
                .email(dto.getEmail())
                .username(dto.getUsername())
                .password(encoder.encode(dto.getPassword()))  // Passwort hashen!
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .age(dto.getAge())
                .dateOfBirth(dto.getDateOfBirth())
                .role(Role.ROLE_USER)
                .phoneNumber(dto.getPhoneNumber())
                .tstamp(LocalDateTime.now())
                .build();

        return userRepository.saveNew(user);
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
