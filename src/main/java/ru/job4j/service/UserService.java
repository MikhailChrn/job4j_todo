package ru.job4j.service;

import ru.job4j.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> save(User user);

    Optional<User> findByLoginAndPassword(String login, String password);

}
