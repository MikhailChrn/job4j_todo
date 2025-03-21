package ru.job4j.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "tasks")
@Data
@Builder
@EqualsAndHashCode(of = {"id", "description"})
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    private static final DateTimeFormatter FORMATTER
            = DateTimeFormatter.ofPattern("dd-MMMM-EEEE-yyyy HH:mm:ss");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String description;

    private LocalDateTime created = LocalDateTime.now();

    private boolean done;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority_id")
    private Priority priority;

}