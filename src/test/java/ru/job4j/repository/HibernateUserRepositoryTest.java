package ru.job4j.repository;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.configuration.HibernateConfiguration;
import ru.job4j.entity.User;
import ru.job4j.exception.RepositoryException;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HibernateUserRepositoryTest {

    private static SessionFactory sessionFactory;

    private static UserRepository userRepository;

    @BeforeAll
    public static void initRepositories() {
        sessionFactory = new HibernateConfiguration().sessionFactory();
        userRepository = new HibernateUserRepository(sessionFactory);
    }

    @AfterEach
    public void clearTasks() {
        userRepository.findAll().forEach(
                user -> userRepository.deleteById(user.getId())
        );
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(userRepository.deleteById(99))
                .isFalse();
    }

    @Test
    public void whenDontSaveThenNothingFoundAndGetRepositoryException() {

        assertThrows(RepositoryException.class,
                () -> {
                    userRepository.findById(0);
                });
    }

    @Test
    public void whenSaveThenGetSame() {
        User user = userRepository.save(
                User.builder().name("name")
                        .login("login")
                        .password("password")
                        .build());

        User savedUser = userRepository
                .findById(user.getId()).get();

        assertThat(savedUser).isEqualTo(user);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        User user1 = userRepository.save(
                User.builder().name("name 1")
                        .login("login 1")
                        .password("password 1").build());
        User user2 = userRepository.save(
                User.builder().name("name 2")
                        .login("login 2")
                        .password("password 2").build());
        User user3 = userRepository.save(
                User.builder().name("name 3")
                        .login("login 3")
                        .password("password 3").build());

        Collection<User> result = userRepository.findAll();

        assertThat(result).isEqualTo(List.of(user1, user2, user3));
    }

    @Test
    public void whenDeleteThenNothingFoundAndGetRepositoryException() {
        User user = userRepository.save(
                User.builder().name("name")
                        .login("login")
                        .password("password").build());

        assertThat(userRepository.deleteById(user.getId())).isTrue();
        assertThrows(RepositoryException.class,
                () -> {
                    userRepository.findById(user.getId());
                });
    }
}