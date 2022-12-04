package ua.foxminded.university.rest_controllers;

import org.springframework.validation.BindingResult;
import ua.foxminded.university.util.exceptions.ValidationException;

public class DefaultController {

    public static final String SERVER_ERROR_DESCRIPTION = "Internal Server Error if any other exception was occurred";

    public void checkForErrors(BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
    }
}

