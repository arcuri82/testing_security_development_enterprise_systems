package org.tsdes.intro.exercises.quizgame.backend.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tsdes.intro.exercises.quizgame.backend.StubApplication;

import static org.junit.Assert.*;

/**
 * Created by arcuri82 on 15-Dec-17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StubApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DefaultDataInitializerServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private QuizService quizService;

    @Test
    public void testInit() {

        assertTrue(categoryService.getAllCategories(false).size() > 0);

        assertTrue(categoryService.getAllCategories(true).stream()
                .mapToLong(c -> c.getSubCategories().size())
                .sum() > 0);

        assertTrue(quizService.getQuizzes().size() > 0);
    }
}