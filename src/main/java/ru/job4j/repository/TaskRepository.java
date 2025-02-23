package ru.job4j.repository;

import ru.job4j.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskRepository {

    Optional<Task> findById(int id);

    Collection<Task> findAll();

    Collection<Task> findAllNew();

    Collection<Task> findAllCompleted();

    Collection<Task> findByLikeDescription(String key);

    Collection<Task> findAllOrderByCreated();

    Task save(Task film);

    boolean deleteById(int id);

    boolean update(Task task);

}