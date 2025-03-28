package ru.job4j.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.repository.CategoryRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleCategoryServiceTest {

    private static CategoryRepository categoryRepository;

    private static CategoryService categoryService;

    @BeforeAll
    public static void initService() {
        categoryRepository = mock(CategoryRepository.class);
        categoryService = new SimpleCategoryService(categoryRepository);
    }

    @Test
    public void whenGetNothingThenFindUserByIdFail() {

        when(categoryRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThat(categoryService.findById(anyInt()))
                .isEqualTo(Optional.empty());
    }

}