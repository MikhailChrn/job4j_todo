package ru.job4j.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.entity.User;
import ru.job4j.service.SimpleUserService;
import ru.job4j.service.UserService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class  IndexControllerTest {

    private UserService userService;

    private HttpSession session;

    private HttpServletRequest request;

    @BeforeEach
    public void initServices() {
        userService = mock(SimpleUserService.class);
        session = mock(HttpSession.class);
        request = mock(HttpServletRequest.class);
    }

    @Test
    public void whenRequestIndexPageThenGetIndexPage() {
        User user = User.builder().id(1).build();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);

        IndexController indexController = new IndexController(userService);

        Model model = new ConcurrentModel();
        String view = indexController.getIndex(model, request);

        assertThat(view).isEqualTo("index");
    }

}