package ru.job4j.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import ru.job4j.entity.Category;
import ru.job4j.entity.Priority;
import ru.job4j.entity.Task;
import ru.job4j.entity.User;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * DTO for {@link Task}
 */
@Data
@AllArgsConstructor
public class TaskDto {
    int id;
    String title;
    User user;
    String description;
    LocalDateTime created;
    boolean done;
    Priority priority;
    Collection<Category> categories;
}