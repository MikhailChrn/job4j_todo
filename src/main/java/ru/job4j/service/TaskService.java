package ru.job4j.service;

import ru.job4j.dto.CreateTaskDto;
import ru.job4j.dto.TaskDto;
import ru.job4j.entity.User;

import java.util.Collection;
import java.util.Optional;

public interface TaskService {

    Collection<TaskDto> findAllByUser(User user);

    Collection<TaskDto> findAllNewByUser(User user);

    Collection<TaskDto> findAllCompletedByUser(User user);

    boolean add(CreateTaskDto dto);

    boolean update(TaskDto dto);

    Optional<TaskDto> findById(int id);

    boolean deleteById(int id);

    boolean updateStatusById(int id, boolean done);

}
