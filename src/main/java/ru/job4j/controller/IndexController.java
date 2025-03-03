package ru.job4j.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.service.UserService;

@Controller
@AllArgsConstructor
public class IndexController {

    private final UserService userService;

    @GetMapping({"/", "/index"})
    public String getIndex(Model model, HttpSession session) {
        userService.addUserAsAttributeToModel(model, session);
        return "index";
    }
}
