package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.job4j.entity.User;
import ru.job4j.exception.RepositoryException;
import ru.job4j.entity.Task;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
    public Collection<Task> findAllByUser(User user) {
        Session session = sessionFactory.openSession();
        List<Task> result = List.of();
        try {
            session.beginTransaction();
            result = session.createQuery("FROM Task T WHERE T.user = :user")
                    .setParameter("user", user)
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
     * @return список задач
     */
    @Override
    public Collection<Task> findAllByDoneByUser(User user, boolean done) {
        Session session = sessionFactory.openSession();
        List<Task> result = List.of();
        try {
            session.beginTransaction();
            result = session.createQuery("FROM Task T "
                            + "WHERE T.userId = :userId "
                            + "AND T.done = :done")
                    .setParameter("user", user)
                    .setParameter("done", done)
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
        try {
            Transaction transaction = session.beginTransaction();
            task.setCreated(LocalDateTime.now());
            session.persist(task);
            transaction.commit();

            return task;

        } catch (Exception ex) {
            session.getTransaction().rollback();

        } finally {
            session.close();
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
     * @param id   задачи
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