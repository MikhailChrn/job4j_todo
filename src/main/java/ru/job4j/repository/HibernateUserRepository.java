package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.entity.User;
import ru.job4j.exception.RepositoryException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateUserRepository implements UserRepository {

    private final SessionFactory sessionFactory;

    /**
     * Сохранить в базе задачу.
     *
     * @param user задача
     * @return Optional задача с id
     */
    @Override
    public User save(User user) {
        Session session = this.sessionFactory.openSession();
        Integer userId;
        User result = null;
        try {
            Transaction transaction = session.beginTransaction();
            userId = (Integer) session.save(user);
            if (userId != null) {
                result = session.get(User.class, userId);
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
     * Список всех пользователей
     *
     * @return список всех пользователей приложения
     */
    @Override
    public Collection<User> findAll() {
        Session session = sessionFactory.openSession();
        List<User> result = List.of();
        try {
            session.beginTransaction();
            result = session.createQuery("FROM User")
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
     * Найти пользователя по ID
     *
     * @return Optional пользователь
     */
    @Override
    public Optional<User> findById(int id) {
        Session session = sessionFactory.openSession();
        Optional<User> result = Optional.empty();
        try {
            session.beginTransaction();
            result = Optional.of(
                    session.get(User.class, id));
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }

        if (result.isEmpty()) {
            throw new RepositoryException(
                    "Пользователь с указанным идентификатором не найден. ");
        }

        return result;
    }

    /**
     * Найти пользователя по login и password
     *
     * @param login password
     * @return Optional пользователь
     */
    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Session session = sessionFactory.openSession();
        Optional<User> result = Optional.empty();
        try {
            session.beginTransaction();
            result = session.createQuery(
                            "FROM User U WHERE U.login = :login "
                                    + "AND U.password = :password", User.class)
                    .setParameter("login", login)
                    .setParameter("password", password)
                    .uniqueResultOptional();
            session.getTransaction().commit();

        } catch (Exception e) {
            session.getTransaction().rollback();

        } finally {
            session.close();
        }

        if (result.isEmpty()) {
            throw new RepositoryException(
                    "Пользователь с указанными идентификатором не найден. ");
        }

        return result;
    }

    /**
     * Удалить пользователя по id
     *
     * @param id пользователя ID
     */
    @Override
    public boolean deleteById(int id) {
        Session session = sessionFactory.openSession();
        boolean result = false;
        try {
            session.beginTransaction();
            int count = session.createQuery("DELETE User U WHERE U.id = :id")
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
}
