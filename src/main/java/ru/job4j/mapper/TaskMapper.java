package ru.job4j.mapper;

import org.mapstruct.Mapper;
import ru.job4j.dto.TaskDto;
import ru.job4j.model.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDto getModelFromEntity(Task task);

    Task getEntityFromDto(TaskDto taskDto);
}
