package at.boot.mail;

import jakarta.annotation.PostConstruct;
import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

@Service
public class MailService {

    private static final Logger logger = Logger.getLogger(MailService.class.getName());

    @Value("${mail.smtp.host}")
    private String smtpHost;

    @Value("${mail.smtp.port}")
    private int smtpPort;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    @Value("${app.confirmation.url}")
    private String confirmationUrl;

    @Value("${app.forgotpassword.url}")
    private String forgotPasswordUrl;

    private Session mailSession;

    @PostConstruct
    public void init() {
        Properties sessionProps = new Properties();
        sessionProps.put("mail.smtp.auth", "true");
        sessionProps.put("mail.smtp.starttls.enable", "true");
        sessionProps.put("mail.smtp.host", smtpHost);
        sessionProps.put("mail.smtp.port", String.valueOf(smtpPort));

        mailSession = Session.getInstance(sessionProps, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        logger.info("MailService initialisiert mit SMTP Host: " + smtpHost + " Port: " + smtpPort);
    }

    public boolean sendConfirmationEmail(String recipient, String token) {
        if (confirmationUrl == null || confirmationUrl.isBlank()) {
            throw new RuntimeException("Property app.confirmation.url nicht gesetzt!");
        }

        String confirmationLink = confirmationUrl + token;
        String subject = "Willkommen bei SliceIt – Bitte bestätige deine Registrierung";
        String htmlContent = loadTemplate("confirmation-email.html", confirmationLink);
        return sendEmail(recipient, subject, htmlContent);
    }

    public boolean sendForgotPasswordEmail(String recipient, String token) {
        if (forgotPasswordUrl == null || forgotPasswordUrl.isBlank()) {
            throw new RuntimeException("Property app.forgotpassword.url nicht gesetzt!");
        }

        String resetLink = forgotPasswordUrl + token;
        String subject = "Passwort zurücksetzen – SliceIt";
        String htmlContent = loadTemplate("forgot-password-email.html", resetLink);
        return sendEmail(recipient, subject, htmlContent);
    }

    public boolean sendEmail(String recipient, String subject, String htmlContent) {
        try {
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html; charset=UTF-8");

            Transport.send(message);
            logger.info("E-Mail gesendet an: " + recipient);
            return true;
        } catch (MessagingException e) {
            logger.severe("Fehler beim Senden der E-Mail: " + e.getMessage());
            return false;
        }
    }

    public boolean isValidEmailSyntax(String input) {
        try {
            InternetAddress emailAddr = new InternetAddress(input);
            emailAddr.validate();
            return true;
        } catch (AddressException ex) {
            return false;
        }
    }

    private String loadTemplate(String templateName, String link) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/" + templateName)) {
            if (inputStream == null) {
                throw new RuntimeException("Template nicht gefunden: " + templateName);
            }
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            return content.replace("{{link}}", link);
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Laden des Templates: " + templateName, e);
        }
    }
}
