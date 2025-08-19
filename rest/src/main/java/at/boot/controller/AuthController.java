package at.boot.controller;

import at.boot.auth.AuthService;
import at.boot.models.User;
import at.boot.requests.ForgotPassDTO;
import at.boot.requests.LoginRequest;
import at.boot.requests.ResetPasswordDTO;
import at.boot.responses.LoginResponse;
import at.boot.responses.MessageResponse;
import at.boot.responses.RegisterUserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // --- REGISTER --- //
    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterUserDTO dto) {
        User user = authService.register(dto);
        return ResponseEntity.ok(user);
    }


    // --- USER LOGIN --- //
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.verify(loginRequest);
    }


    // --- REGISTER EMAIL BESTÄTIGUNG //
    @GetMapping("/confirm")
    public ResponseEntity<?> confirmEmail(@RequestParam("token") String token) {
        try {
            authService.confirmEmail(token);
            return ResponseEntity.ok(new MessageResponse("E-Mail bestätigt. Sie können sich jetzt anmelden."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // --- PASSWORT VERGESSEN --- //
    @PostMapping("/forgot")
    public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody ForgotPassDTO dto) {
        try {
            authService.forgotPassword(dto);
            return ResponseEntity.ok(new MessageResponse(
                    "Wir haben dir eine E-Mail geschickt, mit der du dein Passwort zurücksetzen kannst."
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // --- PASSWORT ZURÜCKSETZEN --- //
    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordDTO dto) {
        try {
            authService.resetPassword(dto);
            return ResponseEntity.ok(new MessageResponse("Ihr Passwort wurde erfolgreich zurückgesetzt."));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}


