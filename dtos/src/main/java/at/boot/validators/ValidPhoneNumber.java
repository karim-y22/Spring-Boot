package at.boot.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumber {
    String message() default "Ungültige Telefonnummer – bitte nur Ziffern und optionales + am Anfang";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
