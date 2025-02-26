package ru.job4j.service;

import ru.job4j.dto.CreateTaskDto;
import ru.job4j.dto.TaskDto;

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

    boolean updateStatusById(int id, boolean done);

}
