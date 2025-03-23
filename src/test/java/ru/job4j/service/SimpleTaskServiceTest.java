package ru.job4j.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.dto.CreateTaskDto;

import org.mapstruct.factory.Mappers;

import ru.job4j.dto.TaskDto;
import ru.job4j.entity.Priority;
import ru.job4j.entity.User;
import ru.job4j.mapper.TaskMapper;
import ru.job4j.entity.Task;
import ru.job4j.repository.PriorityRepository;
import ru.job4j.repository.TaskRepository;
import ru.job4j.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleTaskServiceTest {

    private static TaskRepository taskRepository;
    private static UserRepository userRepository;
    private static PriorityRepository priorityRepository;
    private static TaskMapper taskMapper;
    private static TaskService taskService;

    @BeforeAll
    public static void initServices() {
        taskRepository = mock(TaskRepository.class);
        userRepository = mock(UserRepository.class);
        priorityRepository = mock(PriorityRepository.class);
        taskMapper = Mappers.getMapper(TaskMapper.class);
        taskService = new SimpleTaskService(taskRepository,
                userRepository,
                priorityRepository,
                taskMapper);
    }

    @Test
    public void whenAddTaskThenFindTaskByIdSuccessfull() {
        User user = User.builder().id(1).build();
        Priority priority = Priority.builder().id(1).build();

        CreateTaskDto dto = new CreateTaskDto("test", "Test-description", 1);
        Task result = Task.builder().user(user).description(dto.getDescription()).build();

        when(taskRepository.save(any(Task.class)))
                .thenReturn(Task.builder().description(dto.getDescription()).build());
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(priorityRepository.findById(anyInt())).thenReturn(Optional.of(priority));
        when(taskRepository.findById(anyInt()))
                .thenReturn(Optional.of(result));

        assertThat(taskService.add(dto)).isTrue();
        assertThat(taskService.findById(anyInt()).get())
                .isEqualTo(new TaskDto(result.getId(),
                        result.getTitle(),
                        result.getUser(),
                        result.getDescription(),
                        result.getCreated(),
                        result.isDone(),
                        result.getPriority()));
    }

    @Test
    public void whenGetNothingThenFindTaskByIdFail() {
        when(taskRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThat(taskService.findById(anyInt()))
                .isEqualTo(Optional.empty());
    }

    @Test
    public void whenInvertDoneByIdThenGetTrue() {

        when(taskRepository.updateStatusById(anyInt(), anyBoolean())).thenReturn(true);

        assertThat(taskService.updateStatusById(7, true)).isTrue();
    }

    @Test
    public void whenRequestAllTaskThenReturnCollectionTaskDto() {
        User user = User.builder().id(1).build();

        Collection<Task> taskRepositoryResponse = List.of(
                Task.builder().id(1).title("test1").user(user)
                        .description("test-descr-1").created(LocalDateTime.now())
                        .done(true).build(),
                Task.builder().id(2).title("test2").user(user)
                        .description("test-descr-2").created(LocalDateTime.now())
                        .done(true).build(),
                Task.builder().id(3).title("test3").user(user)
                        .description("test-descr-3").created(LocalDateTime.now())
                        .done(true).build());

        when(taskRepository.findAllByUser(user))
                .thenReturn(taskRepositoryResponse);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        assertThat(taskService.findAllByUser(user).size())
                .isEqualTo(taskRepositoryResponse.size());
        assertThat(taskService.findAllByUser(user).stream()
                .findFirst().get().getClass())
                .isEqualTo(TaskDto.class);
    }
}