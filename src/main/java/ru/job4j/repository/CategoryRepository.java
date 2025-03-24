package ru.job4j.repository;

import ru.job4j.entity.Category;

import java.util.Collection;
import java.util.Optional;

public interface CategoryRepository {

    Collection<Category> findAll();

    Optional<Category> findById(int id);

    Collection<Category> findAllById(Collection<Integer> ids);

}
