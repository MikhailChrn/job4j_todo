package ru.job4j.dto;

import lombok.Value;
import ru.job4j.entity.Task;
import ru.job4j.entity.User;

import java.time.LocalDateTime;

/**
 * DTO for {@link Task}
 */
@Value
public class TaskDto {
    int id;
    String title;
    User user;
    String description;
    LocalDateTime created;
    boolean done;
}