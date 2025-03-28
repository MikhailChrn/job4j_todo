package ru.job4j.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskDto {

    String title;

    String description;

    int userId;

    int priorityId;

    Collection<Integer> categories;

}
