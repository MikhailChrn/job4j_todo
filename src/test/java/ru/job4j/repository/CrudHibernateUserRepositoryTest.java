package ru.job4j.repository;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.job4j.configuration.HibernateConfiguration;
import ru.job4j.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CrudHibernateUserRepositoryTest {

    private static UserRepository userRepository;

    @BeforeAll
    public static void initRepositories() {
        SessionFactory sessionFactory = new HibernateConfiguration().sessionFactory();
        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        userRepository = new CrudHibernateUserRepository(crudRepository);
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
    public void whenDontSaveThenNothingFoundAndGetFalse() {

        assertThat(userRepository.deleteById(0)).isFalse();

    }

    @Test
    public void whenSaveThenGetSame() {
        User user = userRepository.save(User.builder().name("name")
                .login("login")
                .password("password")
                .build()).get();

        User savedUser = userRepository
                .findByLoginAndPassword(user.getLogin(), user.getPassword()).get();

        assertThat(savedUser).isEqualTo(user);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        User user1 = User.builder().name("name 1")
                .login("login 1")
                .password("password 1").build();
        User user2 = User.builder().name("name 2")
                .login("login 2")
                .password("password 2").build();
        User user3 = User.builder().name("name 3")
                .login("login 3")
                .password("password 3").build();

        List.of(user1, user2, user3)
                .forEach(user -> userRepository.save(user));

        Collection<User> result = userRepository.findAll();

        assertThat(result).isEqualTo(List.of(user1, user2, user3));
    }

    @Test
    public void whenDeleteThenNothingFoundAndGetRepositoryException() {
        User user = User.builder().name("name")
                .login("login")
                .password("password").build();

        userRepository.save(user);

        assertThat(userRepository.deleteById(user.getId())).isTrue();

        assertThat(userRepository.findById(user.getId()))
                .isEqualTo(Optional.empty());

    }

    @Test
    public void whenSavedUserThenGetHimByLoginAndPasswordSuccess() {
        User user = User.builder().name("name")
                .login("login")
                .password("password").build();

        userRepository.save(user);

        assertThat(userRepository.findByLoginAndPassword(user.getLogin(), user.getPassword()).get())
                .isEqualTo(user);
    }

}