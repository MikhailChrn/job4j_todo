package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.exception.RepositoryException;
import ru.job4j.model.Task;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HebirnateTaskRepository implements TaskRepository {

    private final SessionFactory sessionFactory;

    /**
     * Найти задачу по ID
     *
     * @return Optional задача
     */
    @Override
    public Optional<Task> findById(int id) {
        Session session = sessionFactory.openSession();
        Optional<Task> result = Optional.empty();
        try {
            session.beginTransaction();
            result = Optional.of(
                    session.get(Task.class, id));
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();

        } finally {
            session.close();
        }

        return result;
    }

    /**
     * Список всех задач
     *
     * @return всех список задач
     */
    @Override
    public Collection<Task> findAll() {
        Session session = sessionFactory.openSession();
        List<Task> result = List.of();
        try {
            session.beginTransaction();
            result = session.createQuery("FROM Task")
                    .getResultList();
            session.getTransaction().commit();

        } catch (Exception ex) {
            session.getTransaction().rollback();

        } finally {
            session.close();
        }

        return result;
    }

    /**
     * Список новых задач
     *
     * @return новых список задач
     */
    @Override
    public Collection<Task> findAllNew() {
        Session session = sessionFactory.openSession();
        List<Task> result = List.of();
        try {
            session.beginTransaction();
            result = session.createQuery("FROM Task T WHERE T.done = false")
                    .getResultList();
            session.getTransaction().commit();

        } catch (Exception ex) {
            session.getTransaction().rollback();

        } finally {
            session.close();
        }

        return result;
    }

    /**
     * Список выполненных задач
     *
     * @return выполненных список задач
     */
    @Override
    public Collection<Task> findAllCompleted() {
        Session session = sessionFactory.openSession();
        List<Task> result = List.of();
        try {
            session.beginTransaction();
            result = session.createQuery("FROM Task T WHERE T.done = true")
                    .getResultList();
            session.getTransaction().commit();

        } catch (Exception ex) {
            session.getTransaction().rollback();

        } finally {
            session.close();
        }

        return result;
    }

    /**
     * Список задач, отфильтрованных по description LIKE %key%
     *
     * @param key key
     * @return список задач
     */
    @Override
    public Collection<Task> findByLikeDescription(String key) {
        Session session = sessionFactory.openSession();
        List<Task> result = List.of();
        try {
            session.beginTransaction();
            result = session.createQuery(
                            "FROM Task T WHERE T.description LIKE :key", Task.class)
                    .setParameter("key", "%" + key + "%")
                    .getResultList();
            session.getTransaction().commit();

        } catch (Exception e) {
            session.getTransaction().rollback();

        } finally {
            session.close();
        }

        return result;
    }

    /**
     * Список задач, отсортированных по created
     *
     * @return список задач
     */
    @Override
    public Collection<Task> findAllOrderByCreated() {
        Session session = sessionFactory.openSession();
        List<Task> result = List.of();
        try {
            session.beginTransaction();
            result = session.createQuery("FROM Task T ORDER BY T.created")
                    .getResultList();
            session.getTransaction().commit();

        } catch (Exception ex) {
            session.getTransaction().rollback();

        } finally {
            session.close();
        }

        return result;
    }

    /**
     * Сохранить в базе задачу.
     *
     * @param task задача
     * @return задача с id
     */
    @Override
    public Task save(Task task) {
        Session session = this.sessionFactory.openSession();
        Integer taskId;
        try {
            Transaction transaction = session.beginTransaction();
            taskId = (Integer) session.save(task);
            transaction.commit();

        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw new RepositoryException(ex.getMessage());

        } finally {
            session.close();
        }

        return taskId != null ? findById(taskId).get() : null;
    }

    /**
     * Удалить задачу по id
     *
     * @param id ID
     */
    @Override
    public boolean deleteById(int id) {
        Session session = sessionFactory.openSession();
        boolean result = false;
        try {
            session.beginTransaction();
            int count = session.createQuery("DELETE Task T WHERE T.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
            result = count > 0 ? true : false;

        } catch (Exception e) {
            session.getTransaction().rollback();

        } finally {
            session.close();
        }

        return result;
    }

    /**
     * Обновить задачу
     *
     * @param task задача
     */
    @Override
    public boolean update(Task task) {
        Session session = sessionFactory.openSession();
        boolean result = false;
        try {
            session.beginTransaction();
            int count = session.createQuery(
                            "UPDATE Task T "
                                    + "SET T.description = :description, "
                                    + "T.created = :created, "
                                    + "T.done = :done "
                                    + "WHERE T.id = :id")
                    .setParameter("description", task.getDescription())
                    .setParameter("created", task.getCreated())
                    .setParameter("done", task.isDone())
                    .setParameter("id", task.getId())
                    .executeUpdate();
            session.getTransaction().commit();
            result = count > 0 ? true : false;

        } catch (Exception ex) {
            session.getTransaction().rollback();

        } finally {
            session.close();
        }

        return result;
    }
}