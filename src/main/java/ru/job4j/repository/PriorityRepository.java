package ru.job4j.repository;

import ru.job4j.entity.Priority;

import java.util.Collection;
import java.util.Optional;

public interface PriorityRepository {

    Collection<Priority> findAll();

    Optional<Priority> findById(int id);

}
