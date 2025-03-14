package ru.job4j.repository;

import ru.job4j.entity.Task;
import ru.job4j.entity.User;

import java.util.Collection;
import java.util.Optional;

public interface TaskRepository {

    Optional<Task> findById(int id);

    Collection<Task> findAllByUser(User user);

    Collection<Task> findAllByDoneByUser(User user, boolean done);

    Collection<Task> findByLikeDescription(String key);

    Collection<Task> findAllOrderByCreated();

    Task save(Task task);

    boolean deleteById(int id);

    boolean update(Task task);

    boolean updateStatusById(int id, boolean done);

}