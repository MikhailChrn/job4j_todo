package ru.job4j.repository;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.configuration.HibernateConfiguration;
import ru.job4j.model.Task;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HebirnateTaskRepositoryTest {

    private static SessionFactory sessionFactory;

    private static TaskRepository taskRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        sessionFactory = new HibernateConfiguration().sessionFactory();
        taskRepository = new HebirnateTaskRepository(sessionFactory);
    }

    @AfterEach
    public void clearTasks() {
        taskRepository.findAll().forEach(
                task -> taskRepository.deleteById(task.getId())
        );
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(taskRepository.deleteById(99))
                .isFalse();
    }

    @Test
    public void whenDontSaveThenNothingFound() {
        assertThat(taskRepository.findAll())
                .isEqualTo(emptyList());
        assertThat(taskRepository.findById(0))
                .isEqualTo(empty());
    }

    @Test
    public void whenSaveThenGetSame() {
        Task task = taskRepository.save(
                new Task("Test-description"));

        Task savedTask = taskRepository
                .findById(task.getId()).get();

        assertThat(savedTask).usingRecursiveComparison()
                .isEqualTo(task);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        Task task1 = taskRepository.save(
                new Task("Test-description 1"));
        Task task2 = taskRepository.save(
                new Task("Test-description 2"));
        Task task3 = taskRepository.save(
                new Task("Test-description 3"));

        Collection<Task> result = taskRepository.findAll();

        assertThat(result).isEqualTo(List.of(task1, task2, task3));
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        Task task = taskRepository.save(
                new Task("Test-description"));

        boolean isDeleted = taskRepository
                .deleteById(task.getId());

        Optional<Task> savedTask = taskRepository
                .findById(task.getId());

        assertThat(isDeleted).isTrue();
        assertThat(savedTask).isEqualTo(empty());
    }

    @Test
    public void whenUpdateThenGetUpdated() {
        Task task = taskRepository.save(
                new Task("before update"));

        Task updatedTask =
                new Task(task.getId(), "after update",
                        task.getCreated(), task.isDone());

        boolean isUpdated = taskRepository.update(updatedTask);
        Task savedTask = taskRepository
                .findById(updatedTask.getId()).get();

        assertThat(isUpdated).isTrue();
        assertThat(savedTask).usingRecursiveComparison()
                .isEqualTo(updatedTask);
    }

    @Test
    public void whenUpdateUnExistingVacancyThenGetFalse() {
        Task task = new Task(-1, "Test-description",
                        LocalDateTime.now(), true);

        boolean isUpdated = taskRepository.update(task);

        assertThat(isUpdated).isFalse();
    }
}