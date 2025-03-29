package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.dto.CreateTaskDto;
import ru.job4j.dto.EditTaskDto;
import ru.job4j.dto.TaskDto;
import ru.job4j.entity.User;
import ru.job4j.mapper.TaskMapper;
import ru.job4j.entity.Task;
import ru.job4j.repository.CategoryRepository;
import ru.job4j.repository.PriorityRepository;
import ru.job4j.repository.TaskRepository;
import ru.job4j.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SimpleTaskService implements TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private final PriorityRepository priorityRepository;

    private final CategoryRepository categoryRepository;

    private final TaskMapper taskMapper;

    private final CategoryService categoryService;

    @Override
    public Collection<TaskDto> findAllByUser(User user) {
        List<TaskDto> taskList = taskRepository
                .findAllByUser(user).stream()
                .map(taskMapper::getDtoFromEntity)
                .collect(Collectors.toList());

        setTimeZone(user, taskList);

        return taskRepository
                .findAllByUser(user).stream()
                .map(taskMapper::getDtoFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<TaskDto> findAllNewByUser(User user) {
        return taskRepository
                .findAllByDoneByUser(user, false).stream()
                .map(taskMapper::getDtoFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<TaskDto> findAllCompletedByUser(User user) {
        return taskRepository
                .findAllByDoneByUser(user, true).stream()
                .map(taskMapper::getDtoFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean add(CreateTaskDto dto) {
        return taskRepository.save(Task.builder()
                .title(dto.getTitle())
                .user(userRepository.findById(dto.getUserId()).get())
                .description(dto.getDescription())
                .created(LocalDateTime.now(ZoneId.of("UTC")))
                .priority(priorityRepository.findById(dto.getPriorityId()).get())
                .categories(categoryService.findAllById(dto.getCategories()))
                .build()) != null;
    }

    @Override
    public boolean update(EditTaskDto dto) {
        return taskRepository.update(Task.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .user(userRepository.findById(dto.getUserId()).get())
                .created(dto.getCreated())
                .priority(priorityRepository.findById(dto.getPriorityId()).get())
                .categories(categoryService.findAllById(dto.getCategories()))
                .build());
    }

    @Override
    public Optional<TaskDto> findById(int id) {
        Optional<Task> taskEntity = taskRepository.findById(id);
        if (taskEntity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(taskMapper
                .getDtoFromEntity(taskEntity.get()));
    }

    @Override
    public boolean deleteById(int id) {
        return taskRepository.deleteById(id);

    }

    @Override
    public boolean updateStatusById(int id, boolean done) {
        return taskRepository.updateStatusById(id, done);
    }

    @Override
    public Optional<EditTaskDto> findEditDtoById(int id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(EditTaskDto.builder().id(taskOptional.get().getId())
                .title(taskOptional.get().getTitle())
                .description(taskOptional.get().getDescription())
                .userId(taskOptional.get().getUser().getId())
                .done(taskOptional.get().isDone())
                .created(taskOptional.get().getCreated())
                .priorityId(taskOptional.get().getPriority().getId())
                .categories(taskOptional.get().getCategories().stream()
                        .map(category -> category.getId())
                        .toList())
                .build());
    }

    private static void setTimeZone(User user, List<TaskDto> taskList) {
        for (TaskDto taskDto : taskList) {
            if (user.getTimezone() == null) {
                user.setTimezone(TimeZone.getDefault().getID());
            }
            taskDto.setCreated(LocalDateTime.from(
                    taskDto.getCreated().atZone(TimeZone.getDefault().toZoneId())
                            .withZoneSameInstant(ZoneId.of(user.getTimezone())))
            );
        }
    }
}
