package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.exception.RepositoryException;
import ru.job4j.entity.Task;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateTaskRepository implements TaskRepository {

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

        if (result.isEmpty()) {
            throw new RepositoryException(
                    "Задача с указанным идентификатором не найдена. ");
        }

        return result;
    }

    /**
     * Список всех задач
     *
     * @return всех список задач
     */
    @Override
    public Collection<Task> findAllByUserId(int userId) {
        Session session = sessionFactory.openSession();
        List<Task> result = List.of();
        try {
            session.beginTransaction();
            result = session.createQuery("FROM Task T WHERE T.userId = :userId")
                    .setParameter("userId", userId)
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
    public Collection<Task> findAllNewByUserId(int userId) {
        Session session = sessionFactory.openSession();
        List<Task> result = List.of();
        try {
            session.beginTransaction();
            result = session.createQuery("FROM Task T "
                            + "WHERE T.userId = :userId "
                            + "AND T.done = false")
                    .setParameter("userId", userId)
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
    public Collection<Task> findAllCompletedByUserId(int userId) {
        Session session = sessionFactory.openSession();
        List<Task> result = List.of();
        try {
            session.beginTransaction();
            result = session.createQuery("FROM Task T "
                            + "WHERE T.userId = :userId "
                            + "AND T.done = true")
                    .setParameter("userId", userId)
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
        Task result = null;
        try {
            Transaction transaction = session.beginTransaction();
            task.setCreated(LocalDateTime.now());
            taskId = (Integer) session.save(task);
            if (taskId != null) {
                result = session.get(Task.class, taskId);
            }
            transaction.commit();

        } catch (Exception ex) {
            session.getTransaction().rollback();
            throw new RepositoryException(ex.getMessage());

        } finally {
            session.close();
        }

        return result;
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
            result = count > 0;

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
                                    + "SET T.title = :title, "
                                    + "T.description = :description "
                                    + "WHERE T.id = :id")
                    .setParameter("title", task.getTitle())
                    .setParameter("description", task.getDescription())
                    .setParameter("id", task.getId())
                    .executeUpdate();
            session.getTransaction().commit();
            result = count > 0;

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
     * @param id задачи
     * @param done требуемый статус задачи
     * @return статус задачи успешно изменён
     */
    @Override
    public boolean updateStatusById(int id, boolean done) {
        Session session = sessionFactory.openSession();
        boolean result = false;
        try {
            session.beginTransaction();
            int count = session.createQuery(
                            "UPDATE Task T "
                                    + "SET T.done = :done "
                                    + "WHERE T.id = :id")
                    .setParameter("done", done)
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
            result = count > 0;

        } catch (Exception ex) {
            session.getTransaction().rollback();

        } finally {
            session.close();
        }

        return result;
    }
}