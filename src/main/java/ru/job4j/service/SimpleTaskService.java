package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.dto.CreateTaskDto;
import ru.job4j.dto.TaskDto;
import ru.job4j.entity.Priority;
import ru.job4j.entity.User;
import ru.job4j.mapper.TaskMapper;
import ru.job4j.entity.Task;
import ru.job4j.repository.TaskRepository;
import ru.job4j.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SimpleTaskService implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    private final UserRepository userRepository;

    @Override
    public Collection<TaskDto> findAllByUser(User user) {
        return taskRepository
                .findAllByUser(user).stream()
                .map(taskMapper::getModelFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<TaskDto> findAllNewByUser(User user) {
        return taskRepository
                .findAllByDoneByUser(user, false).stream()
                .map(taskMapper::getModelFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<TaskDto> findAllCompletedByUser(User user) {
        return taskRepository
                .findAllByDoneByUser(user, true).stream()
                .map(taskMapper::getModelFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean add(CreateTaskDto dto) {
        return taskRepository.save(
                Task.builder()
                        .title(dto.getTitle())
                        .user(userRepository.findById(dto.getUserId()).get())
                        .description(dto.getDescription())
                        .created(LocalDateTime.now())
                        .priority(null)
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
