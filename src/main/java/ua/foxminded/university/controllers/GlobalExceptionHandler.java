package ua.foxminded.university.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import ua.foxminded.university.util.exceptions.EntityNotFoundException;

@Slf4j
@ControllerAdvice(basePackages = {"ua.foxminded.university.controllers"})
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleEntityNotFoundException(Exception ex) {
        log.error(ex.getMessage(), ex);
        ModelAndView modelAndView = new ModelAndView("errors/not-found");
        modelAndView.addObject("message", ex.getLocalizedMessage());
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralException(Exception ex) {
        log.error(ex.getMessage(), ex);
        return "errors/general-error";
    }
}
