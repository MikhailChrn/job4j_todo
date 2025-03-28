package ru.job4j.repository;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.job4j.configuration.HibernateConfiguration;
import ru.job4j.entity.Category;
import ru.job4j.entity.Priority;
import ru.job4j.entity.Task;
import ru.job4j.entity.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CrudHibernateTaskRepositoryTest {

    private static TaskRepository taskRepository;

    private static UserRepository userRepository;

    private static PriorityRepository priorityRepository;

    private static CategoryRepository categoryRepository;

    private static User user;

    private static Priority priority;

    @BeforeAll
    public static void initRepositories() {
        SessionFactory sessionFactory = new HibernateConfiguration().sessionFactory();
        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        taskRepository = new CrudHibernateTaskRepository(crudRepository);
        userRepository = new CrudHibernateUserRepository(crudRepository);
        priorityRepository = new CrudHibernarePriorityRepository(crudRepository);
        categoryRepository = new CrudHibernateCategoryRepository(crudRepository);

        user = userRepository.save(User.builder().name("name")
                .login("login").password("password").build()).get();
        priority = priorityRepository.findById(1).get();

    }

    @AfterEach
    public void clearTasks() {
        taskRepository.findAllByUser(user).forEach(
                task -> taskRepository.deleteById(task.getId())
        );
    }

    @Test
    public void whenDeleteByInvalidIdThenGetFalse() {
        assertThat(taskRepository.deleteById(99))
                .isFalse();
    }

    @Test
    public void whenDontSaveThenNothingFoundAndEmpty() {

        assertThat(taskRepository.findById(0)).isEqualTo(Optional.empty());
    }

    @Test
    public void whenSaveThenGetSame() {
        Task task = taskRepository.save(
                Task.builder().title("Test").user(user)
                        .description("Test-description")
                        .priority(priority)
                        .build());

        Task savedTask = taskRepository
                .findById(task.getId()).get();

        assertThat(savedTask).isEqualTo(task);
    }

    @Test
    public void whenSaveSeveralThenGetAll() {
        Task task1 = taskRepository.save(
                Task.builder().title("Test 1").user(user)
                        .description("Test-description 1")
                        .done(true).priority(priority).build());
        Task task2 = taskRepository.save(
                Task.builder().title("Test 2").user(user)
                        .description("Test-description 2")
                        .done(false).priority(priority).build());
        Task task3 = taskRepository.save(
                Task.builder().title("Test 3").user(user)
                        .description("Test-description 3")
                        .done(true).priority(priority).build());

        Collection<Task> result = taskRepository.findAllByUser(user);

        assertThat(result).isEqualTo(List.of(task1, task2, task3));
    }

    @Test
    public void whenDeleteThenGetEmptyOptional() {
        Task task = taskRepository.save(
                Task.builder().title("Test").user(user)
                        .description("Test-description").build());

        assertThat(taskRepository.deleteById(task.getId())).isTrue();
        assertThat(taskRepository.findById(task.getId()))
                .isEqualTo(Optional.empty());
    }

    @Test
    public void whenUpdateThenGetUpdated() {
        Task task = taskRepository.save(
                Task.builder()
                        .title("title")
                        .user(user)
                        .description("before update")
                        .priority(priority)
                        .build());

        Task updatedTask = Task.builder().id(task.getId())
                .title(task.getTitle())
                .user(task.getUser())
                .description("after update")
                .created(task.getCreated())
                .done(task.isDone())
                .priority(task.getPriority()).build();

        boolean isUpdated = taskRepository.update(updatedTask);
        Task savedTask = taskRepository
                .findById(updatedTask.getId()).get();

        assertThat(isUpdated).isTrue();
        assertThat(savedTask).isEqualTo(updatedTask);
    }

    @Test
    public void whenGetOnlyCompletedTaskThenGetThese() {
        Task task1 = Task.builder().title("Test 1")
                .user(user)
                .description("Test-description-1")
                .done(true).priority(priority).build();
        Task task2 = Task.builder().title("Test 2")
                .user(user)
                .description("Test-description-2")
                .done(false).priority(priority).build();
        Task task3 = Task.builder().title("Test 3")
                .user(user)
                .description("Test-description-3")
                .done(true).priority(priority).build();
        List.of(task1, task2, task3).forEach(
                task -> taskRepository.save(task)
        );

        Collection<Task> result = taskRepository.findAllByUser(user);

        assertThat(result).isEqualTo(List.of(task1, task2, task3));
    }

    @Test
    public void whenTryToUpdateDoneStatusThenGetSuccess() {
        Task originalTask = taskRepository.save(Task.builder()
                .title("Test-title")
                .user(user)
                .description("Test-description")
                .created(LocalDateTime.now())
                .done(false)
                .priority(priority)
                .build());

        taskRepository.updateStatusById(originalTask.getId(), true);

        assertThat(taskRepository.findById(originalTask.getId()).get().isDone()).isTrue();
    }

}