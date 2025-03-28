package ru.job4j.service;

import ru.job4j.entity.Category;

import java.util.Collection;
import java.util.Optional;

public interface CategoryService {

    Optional<Category> findById(int id);

    Collection<Category> findAll();

    Collection<Category> findAllById(Collection<Integer> ids);

}
