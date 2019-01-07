package org.tsdes.intro.exercises.quizgame.entity;



import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class QuizEntityTest {

    @Test
    public void testQuiz(){

        Quiz quiz = new Quiz();
        quiz.setQuestion("Will this test pass?");
        quiz.setFirstAnswer("Yes");
        quiz.setSecondAnswer("No");
        quiz.setThirdAnswer("Maybe");
        quiz.setFourthAnswer("No idea");
        quiz.setIndexOfCorrectAnswer(0);

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("DB");
        EntityManager em = factory.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        em.persist(quiz);

        tx.commit();
    }

}