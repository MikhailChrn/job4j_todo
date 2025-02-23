package ru.job4j.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.dto.CreateTaskDto;

import org.mapstruct.factory.Mappers;

import ru.job4j.dto.TaskDto;
import ru.job4j.mapper.TaskMapper;
import ru.job4j.model.Task;
import ru.job4j.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleTaskServiceTest {

    private static TaskRepository taskRepository;
    private static TaskMapper taskMapper;
    private static TaskService taskService;

    @BeforeAll
    public static void initServices() {
        taskRepository = mock(TaskRepository.class);
        taskMapper = Mappers.getMapper(TaskMapper.class);
        taskService = new SimpleTaskService(taskRepository, taskMapper);
    }

    @Test
    public void whenAddTaskThenFindTaskByIdSuccessfull() {
        CreateTaskDto dto = new CreateTaskDto("Test-description");
        Task result = new Task(dto.getDescription());

        when(taskRepository.save(any(Task.class)))
                .thenReturn(new Task(dto.getDescription()));
        when(taskRepository.findById(anyInt()))
                .thenReturn(Optional.of(result));

        assertThat(taskService.add(dto)).isTrue();
        assertThat(taskService.findById(anyInt()).get())
                .isEqualTo(new TaskDto(result.getId(),
                        result.getDescription(),
                        result.getCreated(),
                        result.isDone()));
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
        Task task = new Task("Test-description");

        when(taskRepository.findById(anyInt())).thenReturn(Optional.of(task));
        when(taskRepository.update(any(Task.class)))
                .thenReturn(true);

        assertThat(taskService.invertDoneById(anyInt())).isTrue();
    }

    @Test
    public void whenRequestAllTaskThenReturnCollectionTaskDto() {
        Collection<Task> taskRepositoryResponse = List.of(
                new Task(1, "test1", LocalDateTime.now(), true),
                new Task(2, "test2", LocalDateTime.now(), true),
                new Task(3, "test3", LocalDateTime.now(), true));

        when(taskRepository.findAll())
                .thenReturn(taskRepositoryResponse);

        assertThat(taskService.findAll().size())
                .isEqualTo(taskRepositoryResponse.size());
        assertThat(taskService.findAll().stream()
                .findFirst().get().getClass())
                .isEqualTo(TaskDto.class);
    }
}