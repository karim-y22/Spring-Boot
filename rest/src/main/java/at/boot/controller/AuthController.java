package at.boot.controller;

import at.boot.auth.AuthService;
import at.boot.requests.LoginRequest;
import at.boot.responses.LoginResponse;
import at.boot.responses.MessageResponse;
import at.boot.responses.RegisterUserDTO;
import jakarta.validation.Valid;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserDTO dto) {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("email", dto.getEmail());
            variables.put("username", dto.getUsername());
            variables.put("password", dto.getPassword());
            variables.put("firstname", dto.getFirstname());
            variables.put("lastname", dto.getLastname());
            variables.put("age", dto.getAge());
            variables.put("dateOfBirth", dto.getDateOfBirth());
            variables.put("phoneNumber", dto.getPhoneNumber());

            ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("register_user_process", variables);
            return ResponseEntity.ok(new MessageResponse("Registrierungsprozess gestartet, Prozess-ID: " + processInstance.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MessageResponse("Fehler beim Starten des Registrierungsprozesses: " + e.getMessage()));
        }
    }


    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.verify(loginRequest);
    }


    @GetMapping("/confirm")
    public ResponseEntity<?> confirmEmail(@RequestParam("token") String token) {
        try {
            // Suche Prozessinstanz anhand des Tokens (z.B. über Prozessvariable)
            ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                    .variableValueEquals("token", token)
                    .singleResult();

            if (pi == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Kein laufender Registrierungsprozess für Token gefunden."));
            }

            // Message an die Prozessinstanz senden, damit sie weitermacht
            runtimeService.createMessageCorrelation("UserConfirmation")
                    .processInstanceId(pi.getId())
                    .setVariable("token", token)
                    .correlate();

            return ResponseEntity.ok(new MessageResponse("Registrierung bestätigt."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Fehler bei der Bestätigung: " + e.getMessage()));
        }
    }
}


