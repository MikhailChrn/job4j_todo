package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.dto.CreateTaskDto;
import ru.job4j.dto.TaskDto;
import ru.job4j.mapper.TaskMapper;
import ru.job4j.model.Task;
import ru.job4j.repository.TaskRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SimpleTaskService implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    @Override
    public Collection<TaskDto> findAll() {
        return taskRepository.findAll().stream()
                .map(taskMapper::getModelFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<TaskDto> findAllNew() {
        return taskRepository.findAllNew().stream()
                .map(taskMapper::getModelFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<TaskDto> findAllCompleted() {
        return taskRepository.findAllCompleted().stream()
                .map(taskMapper::getModelFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean add(CreateTaskDto dto) {
        return taskRepository.save(
                new Task(dto.getDescription())) != null;
    }

    @Override
    public boolean update(TaskDto taskDto) {
        return taskRepository.update(
                taskMapper.getEntityFromDto(taskDto));
    }

    @Override
    public Optional<TaskDto> findById(int id) {
        Optional<Task> taskEntity = taskRepository.findById(id);
        if (taskEntity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(taskMapper
                .getModelFromEntity(taskEntity.get()));
    }

    @Override
    public boolean deleteById(int id) {
        return taskRepository.deleteById(id);

    }

    @Override
    public boolean invertDoneById(int id) {
        Optional<Task> taskModelOptional = taskRepository.findById(id);
        if (taskModelOptional.isEmpty()) {
            return false;
        }
        Task task = taskModelOptional.get();
        task.setDone(!task.isDone());
        return taskRepository.update(task);
    }
}
