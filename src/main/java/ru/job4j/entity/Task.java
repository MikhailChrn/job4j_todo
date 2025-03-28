package ru.job4j.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tasks")
@Data
@Builder
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    /**
     * Идентификатор задачи
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Заголовок задачи
     */
    private String title;

    /**
     * Внешний ключ для пользователя задачи
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Описание задачи
     */
    private String description;

    /**
     * Дата создания
     */
    private LocalDateTime created = LocalDateTime.now();

    /**
     * Статус выполнения задачи
     */
    private boolean done;

    /**
     * Внешний ключ на приоритет
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority_id")
    private Priority priority;

    /**
     * Список категорий задач
     */
    @ManyToMany
    @JoinTable(
            name = "task_category",
            joinColumns = {@JoinColumn(name = "task_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    private Collection<Category> categories;

}