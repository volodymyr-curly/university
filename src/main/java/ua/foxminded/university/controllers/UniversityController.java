package ua.foxminded.university.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class UniversityController {

    @GetMapping("/")
    public String showAll() {
        return "index";
    }
}
