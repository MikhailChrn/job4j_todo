package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.entity.Priority;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CrudHibernarePriorityRepository implements PriorityRepository {

    private final CrudRepository crudRepository;

    /**
     * Список всех возможных приоритетов
     *
     * @return список всех возможных приоритетов
     */
    @Override
    public Collection<Priority> findAll() {

        return crudRepository.query("FROM Priority", Priority.class);
    }

    /**
     * Найти приоритет по ID
     *
     * @return Optional приоритет
     */
    @Override
    public Optional<Priority> findById(int id) {

        return crudRepository.optional("FROM Priority p WHERE p.id = :fId",
                Priority.class, Map.of("fId", id));
    }
}
