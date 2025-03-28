package ru.job4j.service;

import ru.job4j.entity.Priority;

import java.util.Collection;
import java.util.Optional;

public interface PriorityService {

    Optional<Priority> findById(int id);

    Collection<Priority> findAll();

}
