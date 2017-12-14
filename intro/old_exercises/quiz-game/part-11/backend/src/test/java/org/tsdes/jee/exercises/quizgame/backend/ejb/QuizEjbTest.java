package org.tsdes.jee.exercises.quizgame.backend.ejb;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by arcuri82 on 06-Dec-17.
 */
@RunWith(Arquillian.class)
public class QuizEjbTest extends EjbTestBase{

    private long createCtgAndSub(String ctg, String sub){
        long ctgId = categoryEjb.createCategory(ctg);
        return categoryEjb.createSubCategory(ctgId, sub);
    }

    @Test
    public void testNoQuiz(){

        assertEquals(0, quizEjb.getQuizzes().size());
    }

    @Test
    public void testCreateQuiz(){

        long subId = createCtgAndSub("foo","bar");

        String question = "Will this test work?";

        long quizId = quizEjb.createQuiz(subId, question, "yes", "no", "may", "no idea", 0);

        assertEquals(1, quizEjb.getQuizzes().size());
        assertEquals(question, quizEjb.getQuiz(quizId).getQuestion());
    }

}