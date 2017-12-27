package org.tsdes.intro.exercises.quizgame.backend.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.tsdes.intro.exercises.quizgame.backend.StubApplication;
import org.tsdes.intro.exercises.quizgame.backend.entity.Quiz;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    private long createQuizzes(String... names){

        long subId = createCtgAndSub("foo","bar");

        for(String n : names){
            quizService.createQuiz(subId, n, "yes", "no", "may", "no idea", 0);
        }

        return categoryService.getSubCategory(subId).getParent().getId();
    }

    @Test
    public void testNotEnoughQuizzes(){

        long ctgId = createQuizzes("a", "b", "c");

        try{
            quizService.getRandomQuizzes(5, ctgId);
            fail();
        }catch (IllegalArgumentException e){
            //expected
        }
    }

    @Test
    public void testGetRandomQuizzes(){

        long ctgId = createQuizzes("a", "b", "c");

        Set<String> questions = new HashSet<>();

        for(int i=0; i<50; i++){

            List<Quiz> quizzes = quizService.getRandomQuizzes(2, ctgId);
            assertEquals(2, quizzes.size());

            Quiz first = quizzes.get(0);
            Quiz second = quizzes.get(1);

            assertNotEquals(first.getQuestion(), second.getQuestion());

            questions.add(first.getQuestion());
            questions.add(second.getQuestion());
        }

        assertEquals(3, questions.size());
        assertTrue(questions.contains("a"));
        assertTrue(questions.contains("b"));
        assertTrue(questions.contains("c"));
    }
}