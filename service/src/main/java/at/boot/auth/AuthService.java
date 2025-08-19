package at.boot.auth;

import at.boot.enums.Role;
import at.boot.exceptions.EmailNotConfirmedException;
import at.boot.jwt.JWTService;
import at.boot.mail.MailService;
import at.boot.models.User;
import at.boot.repositories.UserRepository;
import at.boot.requests.ForgotPassDTO;
import at.boot.requests.LoginRequest;
import at.boot.requests.ResetPasswordDTO;
import at.boot.responses.LoginResponse;
import at.boot.responses.RegisterUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    MailService mailService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    // --- REGISTER --- //
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

        // Token, Bestätigung setzen
        String token = UUID.randomUUID().toString();
        user.setConfirmed(false);
        user.setConfirmationToken(token);

        // Speichern
        userRepository.saveNew(user);

        // Bestätigungsmail senden
        mailService.sendConfirmationEmail(user.getEmail(), token);

        return user;
    }

    // --- REGISTER EMAIL BESTÄTIGUNG //
    public void confirmEmail(String token) {
        Optional<User> userOpt = userRepository.findByConfirmationToken(token);

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid or expired confirmation token.");
        }

        User user = userOpt.get();
        user.setConfirmed(true);
        user.setConfirmationToken(null);
        userRepository.update(user);
    }


    // --- USER LOGIN --- //
    public LoginResponse verify(LoginRequest loginRequest) {
        try {
            // Benutzer aus der Datenbank abrufen
            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("username or email not found"));

            // Überprüfen, ob die E-Mail des Benutzers bestätigt wurde
            if (!user.isConfirmed()) {
                throw new EmailNotConfirmedException("Bitte bestätigen Sie zuerst Ihre E-Mail.");
            }

            // Authentifizierung mit dem Passwort
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            if (authenticate.isAuthenticated()) {
                String token = jwtService.generateToken(loginRequest.getUsername());

                return new LoginResponse(token, user.getUsername(), user.getRole());
            } else {
                throw new BadCredentialsException("Username or password is incorrect");
            }
        } catch (AuthenticationException ex) {
            // Die Ausnahme wird vom GlobalExceptionHandler abgefangen
            throw ex;
        }
    }

    // --- PASSWORT VERGESSEN --- //
    public void forgotPassword(ForgotPassDTO dto) {
        String emailOrUsername = dto.getEmailOrUsername();

        Optional<User> userOpt = mailService.isValidEmailSyntax(emailOrUsername)
                ? userRepository.findByEmail(emailOrUsername)
                : userRepository.findByUsername(emailOrUsername);

        if (userOpt.isEmpty()) {
            throw new BadCredentialsException("E-Mail oder Benutzername nicht vorhanden");
        }

        User user = userOpt.get();

        // Token generieren
        String token = UUID.randomUUID().toString();

        // Token + Ablauf speichern
        user.setForgotPasswordToken(token);
        user.setForgotPasswordTokenExpiry(LocalDateTime.now().plusMinutes(30));
        userRepository.update(user);

        mailService.sendForgotPasswordEmail(user.getEmail(), token);
    }


    // --- PASSWORT ZURÜCKSETZEN --- //
    public void resetPassword(ResetPasswordDTO dto) {

        // Token, neues Passwort und Bestätigung extrahieren
        String token = dto.getToken();
        String newPassword = dto.getNewPassword();
        String confirmNewPassword = dto.getConfirmNewPassword();

        // Passwort bestätigen kontrolle
        if (!newPassword.equals(confirmNewPassword)) {
            throw new BadCredentialsException("Passwörter stimmen nicht überein");
        }

        // Benutzer mit gültigem Token finden
        User user = userRepository.findByForgotPasswordToken(token)
                .orElseThrow(() -> new BadCredentialsException("Ungültiger oder abgelaufener Token"));

        // Token-Ablaufzeit prüfen
        if (user.getForgotPasswordTokenExpiry() == null || user.getForgotPasswordTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token ist abgelaufen");
        }

        // Neues Passwort hashen und setzen
        String hashedPassword = BCrypt.hashpw(dto.getConfirmNewPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        // Token invalidieren
        user.setForgotPasswordToken(null);
        user.setForgotPasswordTokenExpiry(null);

        // Speichern
        userRepository.update(user);
    }
}
