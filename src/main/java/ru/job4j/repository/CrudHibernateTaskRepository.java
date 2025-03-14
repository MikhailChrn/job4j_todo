package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.entity.Task;
import ru.job4j.entity.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CrudHibernateTaskRepository implements TaskRepository {

    private final CrudRepository crudRepository;

    /**
     * Найти задачу по ID
     *
     * @return Optional задача
     */
    @Override
    public Optional<Task> findById(int id) {

        return crudRepository.optional("FROM Task T WHERE T.id = :fId",
                Task.class, Map.of("fId", id));
    }

    /**
     * Список всех задач
     *
     * @return всех список задач
     */
    @Override
    public Collection<Task> findAllByUser(User user) {

        return crudRepository.query("FROM Task T WHERE T.user = :user",
                Task.class, Map.of("user", user));
    }

    /**
     * Список новых задач
     *
     * @return новых список задач
     */
    @Override
    public Collection<Task> findAllNewByUser(User user) {

        return crudRepository.query("FROM Task T WHERE T.user = :user "
                + "AND T.done = false", Task.class, Map.of("user", user));
    }

    /**
     * Список выполненных задач
     *
     * @return выполненных список задач
     */
    @Override
    public Collection<Task> findAllCompletedByUser(User user) {

        return crudRepository.query("FROM Task T WHERE T.user = :user "
                + "AND T.done = true", Task.class, Map.of("user", user));
    }

    /**
     * Список задач, отфильтрованных по description LIKE %key%
     *
     * @param key key
     * @return список задач
     */
    @Override
    public Collection<Task> findByLikeDescription(String key) {

        return crudRepository.query("FROM Task T WHERE T.description LIKE :key",
                Task.class, Map.of("key", "%" + key + "%"));
    }

    /**
     * Список задач, отсортированных по created
     *
     * @return список задач
     */
    @Override
    public Collection<Task> findAllOrderByCreated() {

        return crudRepository.query("FROM Task T ORDER BY T.created "
                + "WHERE T.userId = :fUserId", Task.class, Map.of("fUserId", 0));
    }

    /**
     * Сохранить в базе задачу.
     *
     * @param task задача
     * @return задача без ID
     */
    @Override
    public Task save(Task task) {
        try {
            crudRepository.run(session -> session.persist(task));
            return task;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Удалить задачу по id
     *
     * @param id ID
     */
    @Override
    public boolean deleteById(int id) {
        int count = crudRepository.tx(session ->
                session.createQuery("DELETE Task T WHERE T.id = :fId")
                        .setParameter("fId", id)
                        .executeUpdate()
        );

        return count > 0;
    }

    /**
     * Обновить задачу
     *
     * @param task задача
     */
    @Override
    public boolean update(Task task) {

        int count = crudRepository.tx(session ->
                session.createQuery("UPDATE Task T SET T.title = :fTitle, "
                                + "T.description = :fDescription "
                                + "WHERE T.id = :fId")
                        .setParameter("fId", task.getId())
                        .setParameter("fTitle", task.getTitle())
                        .setParameter("fDescription", task.getDescription())
                        .executeUpdate()
        );

        return count > 0;
    }

    /**
     * Сохранить в базе задачу.
     *
     * @param id   задачи
     * @param done требуемый статус задачи
     * @return статус задачи успешно изменён
     */
    @Override
    public boolean updateStatusById(int id, boolean done) {

        int count = crudRepository.tx(session ->
                session.createQuery("UPDATE Task T SET T.done = :fDone "
                                + "WHERE T.id = :fId")
                        .setParameter("fDone", done)
                        .setParameter("fId", id)
                        .executeUpdate()
        );

        return count > 0;
    }
}
