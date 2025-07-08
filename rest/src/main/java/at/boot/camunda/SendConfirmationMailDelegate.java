package at.boot.camunda;

import at.boot.auth.AuthService;
import at.boot.models.User;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendConfirmationMailDelegate implements JavaDelegate {

    @Autowired
    private AuthService authService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long userId = (Long) delegateExecution.getVariable("userId");

        User user = authService.findUserById(userId);
        if (user != null && user.getConfirmationToken() != null) {
            authService.sendConfirmationMail(user.getEmail(), user.getConfirmationToken());
        } else {
            throw new IllegalStateException("User oder Confirmation Token nicht gefunden");
        }
    }
}
