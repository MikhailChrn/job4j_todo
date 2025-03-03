package ru.job4j.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.entity.User;
import ru.job4j.service.UserService;

import java.util.Optional;

@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String getRegister() {
        return "users/register";
    }

    @PostMapping("/register")
    public String register(Model model, @ModelAttribute User user) {
        Optional<User> savedUser = userService.save(user);
        if (savedUser.isEmpty()) {
            model.addAttribute("message",
                    "Пользователь с таким логином уже существует");

            return "redirect:/users/register";
        }

        return "/index";
    }

    @GetMapping("/login")
    public String getLoginPage() {

        return "/users/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model,
                            HttpServletRequest request) {
        Optional<User> userOptional = userService
                .findByLoginAndPassword(user.getLogin(), user.getPassword());

        if (userOptional.isEmpty()) {
            model.addAttribute("error",
                    "Логин или пароль введены неверно");
            return "redirect:/users/login";
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", userOptional.get());

        return "/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();

        return "/users/login";
    }

}
