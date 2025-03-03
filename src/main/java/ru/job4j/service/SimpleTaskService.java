package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.dto.CreateTaskDto;
import ru.job4j.dto.TaskDto;
import ru.job4j.mapper.TaskMapper;
import ru.job4j.entity.Task;
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
    public Collection<TaskDto> findAllByUserId(int userId) {
        return taskRepository.findAllByUserId(userId).stream()
                .map(taskMapper::getModelFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<TaskDto> findAllNewByUserId(int userId) {
        return taskRepository.findAllNewByUserId(userId).stream()
                .map(taskMapper::getModelFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<TaskDto> findAllCompletedByUserId(int userId) {
        return taskRepository.findAllCompletedByUserId(userId).stream()
                .map(taskMapper::getModelFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean add(CreateTaskDto dto) {
        return taskRepository.save(
                Task.builder()
                        .title(dto.getTitle())
                        .userId(dto.getUserId())
                        .description(dto.getDescription())
                        .build()) != null;
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
    public boolean updateStatusById(int id, boolean done) {
        return taskRepository.updateStatusById(id, done);
    }
}
