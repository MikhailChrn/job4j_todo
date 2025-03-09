package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.entity.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CrudHibernateUserRepository implements UserRepository {

    private final CrudRepository crudRepository;

    /**
     * Сохранить в базе задачу.
     *
     * @param user задача
     * @return Optional задача (без актуального ID)
     */
    @Override
    public Optional<User> save(User user) {
        crudRepository.run(session -> session.persist(user));
        return Optional.of(user);
    }

    /**
     * Список всех пользователей
     *
     * @return список всех пользователей приложения
     */
    @Override
    public Collection<User> findAll() {
        return crudRepository.query("FROM User", User.class);
    }

    /**
     * Найти пользователя по ID
     *
     * @return Optional пользователь
     */
    @Override
    public Optional<User> findById(int id) {

        return crudRepository.optional("FROM User U WHERE U.id = :fId",
                User.class, Map.of("fId", id));

    }


    /**
     * Найти пользователя по login и password
     *
     * @param login password
     * @return Optional пользователь
     */
    @Override
    public Optional<User> findByLoginAndPassword(String login,
                                                 String password) {
        return crudRepository.query(
                        "FROM User U WHERE U.login = :fLogin "
                                + "AND U.password = :fPassword", User.class,
                        Map.of("fLogin", login, "fPassword", password)
                ).stream().findFirst();
    }

    /**
     * Удалить пользователя по id
     *
     * @param id пользователя ID
     */
    @Override
    public boolean deleteById(int id) {
        int count = crudRepository.tx(session ->
                session.createQuery("DELETE User U WHERE U.id = :id")
                .setParameter("id", id)
                .executeUpdate()
        );

        return count > 0;
    }
}
