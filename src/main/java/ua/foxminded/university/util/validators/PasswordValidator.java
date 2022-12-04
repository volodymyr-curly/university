package ua.foxminded.university.util.validators;

import org.passay.*;
import ua.foxminded.university.util.validators.interfaces.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        org.passay.PasswordValidator validator = new org.passay.PasswordValidator(Arrays.asList(
            new LengthRule(5, 10),
            new WhitespaceRule()
        ));
        RuleResult result = validator.validate(new PasswordData(s));

        if (result.isValid()) {
            return true;
        }

        List<String> messages = validator.getMessages(result);
        String messageTemplate = String.join(" ", messages);
        constraintValidatorContext.buildConstraintViolationWithTemplate(messageTemplate)
            .addConstraintViolation()
            .disableDefaultConstraintViolation();
        return false;
    }
}
