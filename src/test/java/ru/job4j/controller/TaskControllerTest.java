package ru.job4j.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.dto.CreateTaskDto;
import ru.job4j.dto.TaskDto;
import ru.job4j.entity.User;
import ru.job4j.service.SimpleTaskService;
import ru.job4j.service.SimpleUserService;
import ru.job4j.service.TaskService;
import ru.job4j.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskControllerTest {

    private TaskService taskService;

    private UserService userService;

    private TaskController tasksController;

    private Collection<TaskDto> taskDtoList;

    private HttpSession session;

    private HttpServletRequest request;

    @BeforeEach
    public void initServices() {
        taskService = mock(SimpleTaskService.class);
        userService = mock(SimpleUserService.class);
        session = mock(HttpSession.class);
        request = mock(HttpServletRequest.class);
        tasksController = new TaskController(userService, taskService);
    }

    @Test
    public void whenRequestAllTaskThenGetListPageWith() {
        User user = User.builder().id(9).build();

        taskDtoList = List.of(
                new TaskDto(3, "title 3", 9,
                        "Test-descr 3", LocalDateTime.now(), true),
                new TaskDto(5, "title 5", 9,
                        "Test-descr 5", LocalDateTime.now(), false),
                new TaskDto(7, "title 7", 9,
                        "Test-descr 7", LocalDateTime.now(), true));

        when(taskService.findAllByUserId(anyInt())).thenReturn(taskDtoList);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);

        Model model = new ConcurrentModel();
        String view = tasksController.getAllTasksByUserId(model, request);
        Object actualTaskDtos = model.getAttribute("taskDtos");

        assertThat(view).isEqualTo("/tasks/list");
        assertThat(actualTaskDtos).isEqualTo(taskDtoList);
    }

    @Test
    public void whenRequestCreationPageThenGetIt() {
        User user = User.builder().id(9).build();
        Model model = new ConcurrentModel();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        String view = tasksController.getCreationPage(model, request);

        assertThat(view).isEqualTo("/tasks/create");
    }

    @Test
    public void whenSuccessTryToCreateTaskThenRedirectToAllNewTasksPage() {
        User user = User.builder().id(1).build();
        CreateTaskDto createTaskDto = new CreateTaskDto("test", "test-descr", 1);

        when(taskService.add(createTaskDto)).thenReturn(true);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);

        Model model = new ConcurrentModel();
        String view = tasksController.create(createTaskDto, model, request);

        assertThat(view).isEqualTo("redirect:/tasks/all");
    }

    @Test
    public void whenFailTryToCreateTaskThenRedirectToErrorPage() {
        User user = User.builder().id(1).build();
        CreateTaskDto createTaskDto = new CreateTaskDto("test", "test-descr", 1);
        String expectedMassage = "Не удалось создать задачу с указанным идентификатором";

        when(taskService.add(createTaskDto)).thenReturn(false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);

        Model model = new ConcurrentModel();
        String view = tasksController.create(createTaskDto, model, request);
        String actualMessage = (String) model.getAttribute("message");

        assertThat(view).isEqualTo("/errors/404");
        assertThat(actualMessage).isEqualTo(expectedMassage);
    }

    @Test
    public void whenRequestTaskPageThenGetIt() {
        User user = User.builder().id(1).build();
        TaskDto createdTaskDto = new TaskDto(7, "test-7", 1,
                "test-descr-7", LocalDateTime.now(), false);

        when(taskService.findById(createdTaskDto.getId())).thenReturn(Optional.of(createdTaskDto));
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);

        Model model = new ConcurrentModel();
        String view = tasksController.getPageById(createdTaskDto.getId(), model, request);
        Object actualTaskDto = model.getAttribute("taskDto");

        assertThat(view).isEqualTo("/tasks/edit");
        assertThat(actualTaskDto).isEqualTo(createdTaskDto);
    }

    @Test
    public void whenRequestTaskPageThenRedirectToErrorPage() {
        User user = User.builder().id(1).build();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(taskService.findById(anyInt())).thenReturn(Optional.empty());
        String expectedMassage = "Задача с указанным идентификатором не найдена";

        Model model = new ConcurrentModel();
        String view = tasksController.getPageById(user.getId(), model, request);
        String actualMessage = (String) model.getAttribute("message");

        assertThat(view).isEqualTo("/errors/404");
        assertThat(actualMessage).isEqualTo(expectedMassage);
    }

    @Test
    public void whenSuccessEditTaskThenRedirectToAllTasksPage() {
        User user = User.builder().id(1).build();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        TaskDto updatedTaskDto = new TaskDto(7, "test-7", 1,
                "test-descr-7", LocalDateTime.now(), false);

        when(taskService.update(updatedTaskDto)).thenReturn(true);

        Model model = new ConcurrentModel();
        String view = tasksController.update(updatedTaskDto, model, request);

        assertThat(view).isEqualTo("redirect:/tasks/all");
    }

    @Test
    public void whenFailEditTaskThenRedirectToErrorPage() {
        User user = User.builder().id(1).build();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        TaskDto updatedTaskDto = new TaskDto(7, "test-7", 1,
                "test-descr-7", LocalDateTime.now(), false);
        String expectedMassage = "Не удалось изменить задачу с указанным идентификатором";

        when(taskService.update(updatedTaskDto)).thenReturn(false);

        Model model = new ConcurrentModel();
        String view = tasksController.update(updatedTaskDto, model, request);
        String actualMessage = (String) model.getAttribute("message");

        assertThat(view).isEqualTo("/errors/404");
        assertThat(actualMessage).isEqualTo(expectedMassage);
    }

    @Test
    public void whenTryToDeleteTaskThenRedirectToAllTasksPage() {
        User user = User.builder().id(1).build();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(taskService.deleteById(anyInt())).thenReturn(true);

        Model model = new ConcurrentModel();
        String view = tasksController.delete(user.getId(), model, request);

        assertThat(view).isEqualTo("redirect:/tasks/all");
    }

    @Test
    public void whenFailDeleteTaskThenRedirectToErrorPage() {
        String expectedMassage = "Не удалось удалить задачу с указанным идентификатором";

        User user = User.builder().id(1).build();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(taskService.deleteById(anyInt())).thenReturn(false);

        Model model = new ConcurrentModel();
        String view = tasksController.delete(user.getId(), model, request);
        String actualMessage = (String) model.getAttribute("message");

        assertThat(view).isEqualTo("/errors/404");
        assertThat(actualMessage).isEqualTo(expectedMassage);
    }

    @Test
    public void whenTryToMakeTaskDoneThenRedirectToAllTasksPage() {
        User user = User.builder().id(1).build();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(taskService.updateStatusById(anyInt(), anyBoolean())).thenReturn(true);

        Model model = new ConcurrentModel();
        String view = tasksController.changeDoneToFalse(7, model, request);

        assertThat(view).isEqualTo("redirect:/tasks/7");
    }

    @Test
    public void whenTryToMakeTaskDoneThenRedirectToErrorPage() {
        String expectedMassage = "Не удалось обновить статус задачи";

        User user = User.builder().id(1).build();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(taskService.updateStatusById(anyInt(), anyBoolean())).thenReturn(false);

        Model model = new ConcurrentModel();
        String view = tasksController.changeDoneToFalse(7, model, request);
        String actualMessage = (String) model.getAttribute("message");

        assertThat(view).isEqualTo("/errors/404");
        assertThat(actualMessage).isEqualTo(expectedMassage);
    }
}