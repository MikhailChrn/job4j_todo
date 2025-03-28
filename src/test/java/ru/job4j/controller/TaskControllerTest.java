package ru.job4j.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import ru.job4j.dto.CreateTaskDto;
import ru.job4j.dto.EditTaskDto;
import ru.job4j.dto.TaskDto;
import ru.job4j.entity.Category;
import ru.job4j.entity.Priority;
import ru.job4j.entity.User;
import ru.job4j.service.CategoryService;
import ru.job4j.service.PriorityService;
import ru.job4j.service.SimpleTaskService;
import ru.job4j.service.TaskService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskControllerTest {

    private TaskService taskService;

    private CategoryService categoryService;

    private PriorityService priorityService;

    private TaskController tasksController;

    private Collection<TaskDto> taskDtoList;

    private HttpSession session;

    private HttpServletRequest request;

    @BeforeEach
    public void initServices() {
        taskService = mock(SimpleTaskService.class);
        priorityService = mock(PriorityService.class);
        categoryService = mock(CategoryService.class);
        session = mock(HttpSession.class);
        request = mock(HttpServletRequest.class);
        tasksController = new TaskController(taskService,
                categoryService,
                priorityService);
    }

    @Test
    public void whenRequestAllTaskThenGetListPageWith() {
        User user = User.builder().id(9).build();
        Priority priority = Priority.builder().id(1).build();
        List<Category> categories = List.of(Category.builder().id(1).build());

        taskDtoList = List.of(
                new TaskDto(3, "title 3", user,
                        "Test-descr 3", LocalDateTime.now(), true,
                        priority, categories),
                new TaskDto(5, "title 5", user,
                        "Test-descr 5", LocalDateTime.now(), false,
                        priority, categories),
                new TaskDto(7, "title 7", user,
                        "Test-descr 7", LocalDateTime.now(), true,
                        priority, categories));

        when(taskService.findAllByUser(any(User.class))).thenReturn(taskDtoList);
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
        String view = tasksController.getCreationPage(model);

        assertThat(view).isEqualTo("/tasks/create");
    }

    @Test
    public void whenSuccessTryToCreateTaskThenRedirectToAllNewTasksPage() {
        User user = User.builder().id(1).build();
        CreateTaskDto createTaskDto =
                new CreateTaskDto("test", "test-descr",
                        1, 1, List.of(1));

        when(taskService.add(createTaskDto)).thenReturn(true);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);

        Model model = new ConcurrentModel();

        String view = tasksController.create(createTaskDto, request, model);

        assertThat(view).isEqualTo("redirect:/tasks/all");
    }

    @Test
    public void whenFailTryToCreateTaskThenRedirectToErrorPage() {
        User user = User.builder().id(1).build();
        Collection<Category> categories = List.of(Category.builder().id(1).build());
        Priority priority = Priority.builder().id(1).build();
        CreateTaskDto createTaskDto =
                new CreateTaskDto("test", "test-descr",
                        1, 1, List.of(1));
        String expectedMassage = "Не удалось создать задачу с указанным идентификатором";

        when(taskService.add(createTaskDto)).thenReturn(false);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);

        Model model = new ConcurrentModel();

        String view = tasksController.create(createTaskDto, request, model);
        String actualMessage = (String) model.getAttribute("message");

        assertThat(view).isEqualTo("/errors/404");
        assertThat(actualMessage).isEqualTo(expectedMassage);
    }

    @Test
    public void whenRequestTaskPageThenGetIt() {
        User user = User.builder().id(1).build();
        Priority priority = Priority.builder().id(1).build();
        List<Category> categories = List.of(Category.builder().id(1).build());

        TaskDto createdTaskDto = new TaskDto(7, "test-7", user,
                "test-descr-7", LocalDateTime.now(), false,
                priority, categories);

        when(taskService.findById(createdTaskDto.getId())).thenReturn(Optional.of(createdTaskDto));
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);

        Model model = new ConcurrentModel();
        String view = tasksController.getPageById(createdTaskDto.getId(), model);
        Object actualTaskDto = model.getAttribute("taskDto");

        assertThat(view).isEqualTo("/tasks/one");
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
        String view = tasksController.getPageById(user.getId(), model);
        String actualMessage = (String) model.getAttribute("message");

        assertThat(view).isEqualTo("/errors/404");
        assertThat(actualMessage).isEqualTo(expectedMassage);
    }

    @Test
    public void whenSuccessEditTaskThenRedirectToAllTasksPage() {
        User user = User.builder().id(1).build();

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        EditTaskDto updatedTaskDto = new EditTaskDto(7, "test-7", "test-description-7",
                1, LocalDateTime.now(), false, 1, List.of(1));

        when(taskService.update(updatedTaskDto)).thenReturn(true);

        Model model = new ConcurrentModel();

        String view = tasksController.update(updatedTaskDto, model);

        assertThat(view).isEqualTo("redirect:/tasks/" + updatedTaskDto.getId());
    }

    @Test
    public void whenFailEditTaskThenRedirectToErrorPage() {
        User user = User.builder().id(1).build();

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        EditTaskDto updatedTaskDto = new EditTaskDto(7, "test-7", "test-description=7",
                1, LocalDateTime.now(), false, 1, List.of(1));
        String expectedMassage = "Не удалось изменить задачу с указанным идентификатором";

        when(taskService.update(updatedTaskDto)).thenReturn(false);

        Model model = new ConcurrentModel();

        String view = tasksController.update(updatedTaskDto, model);
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
        String view = tasksController.delete(user.getId(), model);

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
        String view = tasksController.delete(user.getId(), model);
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
        String view = tasksController.changeDoneToFalse(7, model);

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
        String view = tasksController.changeDoneToFalse(7, model);
        String actualMessage = (String) model.getAttribute("message");

        assertThat(view).isEqualTo("/errors/404");
        assertThat(actualMessage).isEqualTo(expectedMassage);
    }
}