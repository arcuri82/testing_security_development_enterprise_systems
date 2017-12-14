package org.tsdes.intro.exercises.quizgame.backend.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tsdes.intro.exercises.quizgame.backend.StubApplication;

import static org.junit.Assert.*;

/**
 * Created by arcuri82 on 14-Dec-17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StubApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class QuizServiceTest extends ServiceTestBase{

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private QuizService quizService;

    private long createCtgAndSub(String ctg, String sub){
        long ctgId = categoryService.createCategory(ctg);
        return categoryService.createSubCategory(ctgId, sub);
    }

    @Test
    public void testNoQuiz(){

        assertEquals(0, quizService.getQuizzes().size());
    }

    @Test
    public void testCreateQuiz(){

        long subId = createCtgAndSub("foo","bar");

        String question = "Will this test work?";

        long quizId = quizService.createQuiz(subId, question, "yes", "no", "may", "no idea", 0);

        assertEquals(1, quizService.getQuizzes().size());
        assertEquals(question, quizService.getQuiz(quizId).getQuestion());
    }
}