package ru.job4j.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import ru.job4j.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<Integer> save(User user);

    Optional<User> findByLoginAndPassword(String login, String password);

}
