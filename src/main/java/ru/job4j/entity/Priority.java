package ru.job4j.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Entity
@Table(name = "priorities")
@NoArgsConstructor
@AllArgsConstructor
public class Priority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    private String name;

    private int position;
}
