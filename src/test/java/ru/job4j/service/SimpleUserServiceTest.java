package ru.job4j.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ru.job4j.entity.User;
import ru.job4j.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleUserServiceTest {

    private static UserRepository userRepository;

    private static UserService userService;

    @BeforeAll
    public static void initService() {
        userRepository = mock(UserRepository.class);
        userService = new SimpleUserService(userRepository);
    }

    @Test
    public void whenAddUserThenFindUserByIdSuccessfull() {

        User user = User.builder().id(7)
                .name("name")
                .login("login")
                .password("password").build();

        when(userRepository.save(any(User.class)))
                .thenReturn(Optional.of(user));
        when(userRepository.findByLoginAndPassword(anyString(), anyString()))
                .thenReturn(Optional.of(user));

        assertThat(userService.save(user)).isEqualTo(Optional.of(user));
        assertThat(userService.findByLoginAndPassword(
                user.getLogin(), user.getPassword()).get())
                .isEqualTo(user);
    }

    @Test
    public void whenGetNothingThenFindUserByIdFail() {
        when(userRepository.findByLoginAndPassword(anyString(), anyString()))
                .thenReturn(Optional.empty());

        assertThat(userService.findByLoginAndPassword(anyString(), anyString()))
                .isEqualTo(Optional.empty());
    }
}