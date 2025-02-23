package ru.job4j.dto;

import lombok.Value;
import ru.job4j.model.Task;

import java.time.LocalDateTime;

/**
 * DTO for {@link Task}
 */
@Value
public class TaskDto {

    int id;

    String description;

    LocalDateTime created;

    boolean done;

}