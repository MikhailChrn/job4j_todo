package ru.job4j.repository;

import ru.job4j.entity.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Collection<User> findAll();

    Optional<User> findById(int id);

    Optional<User> findByLoginAndPassword(String login, String password);

    boolean deleteById(int id);
}
