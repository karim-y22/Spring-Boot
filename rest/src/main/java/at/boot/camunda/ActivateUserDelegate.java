package at.boot.camunda;

import at.boot.auth.AuthService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActivateUserDelegate implements JavaDelegate {

    @Autowired
    private AuthService authService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String confirmationToken = (String) delegateExecution.getVariable("token");

        if (confirmationToken == null || confirmationToken.isEmpty()) {
            throw new IllegalArgumentException("Confirmation token is missing");
        }

        authService.confirmEmail(confirmationToken);
    }
}
