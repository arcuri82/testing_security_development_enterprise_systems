package org.tsdes.intro.exercises.quizgame.ejb;

import org.junit.Test;

import javax.ejb.EJB;

import static org.junit.Assert.assertTrue;


public class DefaultDataInitializerEjbTest extends EjbTestBase{



    @EJB
    private CategoryEjb categoryEjb;

    @EJB
    private QuizEjb quizEjb;

    @Test
    public void testInit() {

        assertTrue(categoryEjb.getAllCategories(false).size() > 0);

        assertTrue(categoryEjb.getAllCategories(true).stream()
                .mapToLong(c -> c.getSubCategories().size())
                .sum() > 0);

        assertTrue(quizEjb.getQuizzes().size() > 0);
    }
}