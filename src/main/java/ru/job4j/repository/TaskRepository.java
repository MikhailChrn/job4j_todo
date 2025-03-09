package ru.job4j.repository;

import ru.job4j.entity.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskRepository {

    Optional<Task> findById(int id);

    Collection<Task> findAllByUserId(int userId);

    Collection<Task> findAllNewByUserId(int userId);

    Collection<Task> findAllCompletedByUserId(int userId);

    Collection<Task> findByLikeDescription(String key);

    Collection<Task> findAllOrderByCreated();

    Task save(Task task);

    boolean deleteById(int id);

    boolean update(Task task);

    boolean updateStatusById(int id, boolean done);

}