package ru.job4j.repository;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.configuration.HibernateConfiguration;
import ru.job4j.entity.Task;
import ru.job4j.exception.RepositoryException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HibernateTaskRepositoryTest {

    private static SessionFactory sessionFactory;

    private static TaskRepository taskRepository;

    @BeforeAll
    public static void initRepositories() {
        sessionFactory = new HibernateConfiguration().sessionFactory();
        taskRepository = new HibernateTaskRepository(sessionFactory);
    }

    @AfterEach
    public void clearTasks() {
        taskRepository.findAllByUserId(1).forEach(
                task -> taskRepository.deleteById(task.getId())
        );
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(taskRepository.deleteById(99))
                .isFalse();
    }

    @Test
    public void whenDontSaveThenNothingFoundAndGetRepositoryException() {

        assertThrows(RepositoryException.class,
                () -> {
                    taskRepository.findById(0);
                });
    }

    @Test
    public void whenSaveThenGetSame() {
        Task task = taskRepository.save(
                Task.builder().title("Test").description("Test-description").build());

        Task savedTask = taskRepository
                .findById(task.getId()).get();

        assertThat(savedTask).isEqualTo(task);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        Task task1 = taskRepository.save(
                Task.builder().title("Test 1").userId(1)
                        .description("Test-description 1").build());
        Task task2 = taskRepository.save(
                Task.builder().title("Test 2").userId(1)
                        .description("Test-description 2").build());
        Task task3 = taskRepository.save(
                Task.builder().title("Test 3").userId(1)
                        .description("Test-description 3").build());

        Collection<Task> result = taskRepository.findAllByUserId(1);

        assertThat(result).isEqualTo(List.of(task1, task2, task3));
    }

    @Test
    public void whenDeleteThenGetEmptyOptionalAndGetRepositoryException() {
        Task task = taskRepository.save(
                Task.builder().title("Test").description("Test-description").build());

        assertThat(taskRepository.deleteById(task.getId())).isTrue();
        assertThrows(RepositoryException.class,
                () -> {
                    taskRepository.findById(task.getId());
                });
    }

    @Test
    public void whenUpdateThenGetUpdated() {
        Task task = taskRepository.save(
                Task.builder()
                        .title("title")
                        .userId(1)
                        .description("before update")
                        .build());

        Task updatedTask =
                new Task(task.getId(), task.getTitle(), task.getUserId(), "after update",
                        task.getCreated(), task.isDone());

        boolean isUpdated = taskRepository.update(updatedTask);
        Task savedTask = taskRepository
                .findById(updatedTask.getId()).get();

        assertThat(isUpdated).isTrue();
        assertThat(savedTask).isEqualTo(updatedTask);
    }

    @Test
    public void whenUpdateUnexistingVacancyThenGetFalse() {
        Task task = new Task(-1, "Test-title", 1, "Test-description",
                LocalDateTime.now(), true);

        boolean isUpdated = taskRepository.update(task);

        assertThat(isUpdated).isFalse();
    }

    @Test
    public void whenGetOnlyCompletedTaskThenGetThese() {
        Task task1 = Task.builder().title("Test 1")
                .userId(1)
                .description("Test-description-1")
                .done(true).build();
        Task task2 = Task.builder().title("Test 2")
                .userId(1)
                .description("Test-description-2").build();
        Task task3 = Task.builder().title("Test 3")
                .userId(1)
                .description("Test-description-3")
                .done(true).build();
        List.of(task1, task2, task3).forEach(
                task -> taskRepository.save(task)
        );

        Collection<Task> result = taskRepository.findAllByUserId(1);

        assertThat(result).isEqualTo(List.of(task1, task2, task3));
    }

    @Test
    public void whenTryToUpdateDoneStatusThenGetSuccess() {
        Task originalTask = taskRepository.save(Task.builder()
                .title("Test-title")
                .description("Test-description")
                .created(LocalDateTime.now())
                .done(false)
                .build());

        taskRepository.updateStatusById(originalTask.getId(), true);

        assertThat(taskRepository.findById(originalTask.getId()).get().isDone()).isTrue();
    }
}