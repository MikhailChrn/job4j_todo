package ru.job4j.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditTaskDto {

    int id;

    String title;

    String description;

    int userId;

    LocalDateTime created;

    boolean done;

    int priorityId;

    Collection<Integer> categories;
}
