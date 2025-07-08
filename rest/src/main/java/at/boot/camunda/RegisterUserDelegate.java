package at.boot.camunda;

import at.boot.auth.AuthService;
import at.boot.models.User;
import at.boot.responses.RegisterUserDTO;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RegisterUserDelegate implements JavaDelegate {

    @Autowired
    private AuthService authService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setEmail((String) delegateExecution.getVariable("email"));
        dto.setPassword((String) delegateExecution.getVariable("password"));
        dto.setUsername((String) delegateExecution.getVariable("username"));

        dto.setFirstname((String) delegateExecution.getVariable("firstname"));
        dto.setLastname((String) delegateExecution.getVariable("lastname"));
        dto.setAge((Integer) delegateExecution.getVariable("age"));
        dto.setDateOfBirth((LocalDate) delegateExecution.getVariable("dateOfBirth"));
        dto.setPhoneNumber((String) delegateExecution.getVariable("phoneNumber"));

        User user = authService.register(dto);

        // User ID als Prozessvariable speichern
        delegateExecution.setVariable("userId", user.getId());
        // Token ebenfalls als Prozessvariable setzen**
        delegateExecution.setVariable("token", user.getConfirmationToken());
    }

}
