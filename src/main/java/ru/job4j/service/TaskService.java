package ru.job4j.service;

import ru.job4j.dto.CreateTaskDto;
import ru.job4j.dto.TaskDto;
import ru.job4j.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskService {

    Collection<TaskDto> findAll();

    Collection<TaskDto> findAllNew();

    Collection<TaskDto> findAllCompleted();

    boolean add(CreateTaskDto dto);

    boolean update(TaskDto dto);

    Optional<TaskDto> findById(int id);

    boolean deleteById(int id);

    boolean invertDoneById(int id);

}
