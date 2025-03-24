package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.entity.Category;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CrudHibernateCategoryRepository implements CategoryRepository {

    private final CrudRepository crudRepository;

    /**
     * Список всех возможных категорий
     *
     * @return список всех возможных категорий
     */
    @Override
    public Collection<Category> findAll() {

        return crudRepository.query("FROM Category", Category.class);
    }

    /**
     * Найти категорию по ID
     *
     * @return Optional категорию
     */
    @Override
    public Optional<Category> findById(int id) {

        return crudRepository.optional("""
                FROM Category c 
                WHERE c.id = :fId
                """,
                Category.class,
                Map.of("fId", id));
    }

    /**
     * Найти категории по списку Id
     *
     * @return список категорий
     */
    @Override
    public Collection<Category> findAllById(Collection<Integer> ids) {
        return crudRepository.query("""
                        FROM Category c 
                        WHERE c.id IN :listIds
                        """,
                Category.class,
                Map.of("listIds", ids));
    }
}
