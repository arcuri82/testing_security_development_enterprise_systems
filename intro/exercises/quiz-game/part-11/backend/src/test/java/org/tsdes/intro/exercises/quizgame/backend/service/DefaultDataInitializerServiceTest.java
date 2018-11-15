package org.tsdes.intro.exercises.quizgame.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tsdes.intro.exercises.quizgame.backend.StubApplication;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

/**
 * Created by arcuri82 on 15-Dec-17.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = StubApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
//Important, as referring to state given by Singleton that
//could be modified by previous tests, as they share the same
//SpringBoot application context (ie SpringBoot is started only
//once for all tests).
//Also note that this class does NOT extend ServiceTestBase
@DirtiesContext(classMode = BEFORE_CLASS)
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