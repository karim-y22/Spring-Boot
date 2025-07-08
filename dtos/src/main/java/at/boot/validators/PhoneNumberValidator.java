package at.boot.validators;

import at.boot.util.InputSanitizer;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    private static final String PHONE_PATTERN = "^[+]?\\d{6,20}$";

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        // keine Initialisierung nötig
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.isBlank()) {
            return false; // oder true, falls du @NotBlank separat hast
        }
        String normalized = InputSanitizer.normalizePhoneNumber(value);
        return normalized.matches(PHONE_PATTERN);
    }
}
