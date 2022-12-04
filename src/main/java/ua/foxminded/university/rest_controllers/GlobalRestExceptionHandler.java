package ua.foxminded.university.rest_controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.foxminded.university.domain.ErrorModel;
import ua.foxminded.university.domain.ErrorResponse;
import ua.foxminded.university.util.exceptions.EntityExistsException;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;
import ua.foxminded.university.util.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestControllerAdvice(basePackages = {"ua.foxminded.university.rest_controllers"})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalRestExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEntityNotFoundException(EntityNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return exception.getLocalizedMessage();
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleEntityExistsException(EntityExistsException exception) {
        log.error(exception.getMessage(), exception);
        return exception.getLocalizedMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return exception.getLocalizedMessage();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(ValidationException exception) {
        List<ErrorModel> errorMessages = new ArrayList<>(exception.getBindingResult().getFieldErrors().stream()
            .map(err -> new ErrorModel(err.getField(), err.getRejectedValue(), err.getDefaultMessage()))
            .distinct()
            .toList());

        if (exception.getBindingResult().hasGlobalErrors()){
            ErrorModel globalError = ErrorModel.builder()
                .fieldName("Global error")
                .rejectedValue(Objects.requireNonNull(exception.getBindingResult().getGlobalError()).getObjectName())
                .messageError(exception.getBindingResult().getGlobalError().getDefaultMessage())
                .build();
            errorMessages.add(globalError);
        }

        return ErrorResponse.builder()
            .errorMessage(errorMessages)
            .build();
    }
}
