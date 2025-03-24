package ru.job4j.repository;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.configuration.HibernateConfiguration;
import ru.job4j.entity.Category;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CrudHibernateCategoryRepositoryTest {

    private static CategoryRepository categoryRepository;

    @BeforeAll
    public static void initRepositories() {
        SessionFactory sessionFactory = new HibernateConfiguration().sessionFactory();
        CrudRepository crudRepository = new CrudRepository(sessionFactory);
        categoryRepository = new CrudHibernateCategoryRepository(crudRepository);
    }

    @Test
    public void whenFindJobCategoryPriorityByIdThenGetSuccess() {

        assertThat(categoryRepository.findById(1).get().getTitle())
                .isEqualTo("job");
    }

    @Test
    public void whenFindLearningCategoryByIdThenGetSuccess() {

        assertThat(categoryRepository.findById(2).get().getTitle())
                .isEqualTo("learning");
    }

    @Test
    public void whenFindFamilyCategoryByIdThenGetSuccess() {

        assertThat(categoryRepository.findById(3).get().getTitle())
                .isEqualTo("family");
    }

    @Test
    public void whenFindAllThenGetTwoPriority() {

        assertThat(categoryRepository.findAll().size())
                .isEqualTo(3);
    }

    @Test
    public void whenFindAllByIdListThenGetResult() {

        Collection<Category> result = categoryRepository.findAllById(List.of(3, 1));

        List<Category> expected = List.of(categoryRepository.findById(3).get(),
                categoryRepository.findById(1).get());

        assertTrue(result.size() == expected.size()
                && result.containsAll(expected)
                && expected.containsAll(result));

    }

}