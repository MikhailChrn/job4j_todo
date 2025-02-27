package ru.job4j.dto;

import lombok.Value;

import java.time.LocalDateTime;

/**
 * DTO for {@link ru.job4j.entity.Task}
 */
@Value
public class TaskDto {

    int id;

    String title;

    String description;

    LocalDateTime created;

    boolean done;

}