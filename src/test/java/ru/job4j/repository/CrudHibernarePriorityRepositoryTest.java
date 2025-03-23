package ru.job4j.repository;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.configuration.HibernateConfiguration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CrudHibernarePriorityRepositoryTest {

    private static PriorityRepository priorityRepository;

    @BeforeAll
    public static void initRepositories() {
        SessionFactory sessionFactory = new HibernateConfiguration().sessionFactory();
        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        priorityRepository = new CrudHibernarePriorityRepository(crudRepository);

    }

    @Test
    public void whenFindUrgentlyPriorityByIdThenGetSuccess() {

        assertThat(priorityRepository.findById(1).get().getName())
                .isEqualTo("urgently");
    }

    @Test
    public void whenFindNormalPriorityByIdThenGetSuccess() {

        assertThat(priorityRepository.findById(2).get().getName())
                .isEqualTo("normal");
    }

    @Test
    public void whenFindAllThenGetTwoPriority() {

        assertThat(priorityRepository.findAll().size())
                .isEqualTo(2);
    }

}