package ru.job4j.service;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.job4j.entity.User;
import ru.job4j.repository.UserRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> save(User user) {

        return Optional.of(userRepository.save(user));
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {

        return userRepository.findByLoginAndPassword(login, password);
    }

    @Override
    public void addUserAsAttributeToModel(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }

        model.addAttribute("user", user);
    }
}
