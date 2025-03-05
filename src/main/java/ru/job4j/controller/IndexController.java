package ru.job4j.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.entity.User;
import ru.job4j.service.UserService;

@Controller
@AllArgsConstructor
public class IndexController {

    private final UserService userService;

    @GetMapping({"/", "/index"})
    public String getIndex(Model model, HttpServletRequest request) {
        addUserAsAttributeToModel(model, request);
        return "index";
    }

    public void addUserAsAttributeToModel(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }

        model.addAttribute("user", user);
    }
}
