package ru.job4j.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class  IndexControllerTest {

    @Test
    public void whenRequestIndexPageThenGetIndexPage() {

        IndexController indexController = new IndexController();

        String view = indexController.getIndex();

        assertThat(view).isEqualTo("index");
    }

}