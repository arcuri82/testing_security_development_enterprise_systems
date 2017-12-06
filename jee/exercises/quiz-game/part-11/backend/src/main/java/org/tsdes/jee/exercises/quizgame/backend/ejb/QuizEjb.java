package org.tsdes.jee.exercises.quizgame.backend.ejb;


import org.tsdes.jee.exercises.quizgame.backend.entity.Quiz;
import org.tsdes.jee.exercises.quizgame.backend.entity.SubCategory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Random;

/**
 * Created by arcuri82 on 02-Dec-16.
 */
@Stateless
public class QuizEjb {

    @PersistenceContext
    private EntityManager em;

    private Random random = new Random();

    public long getRandomQuizId(){

        Query query = em.createQuery("select q.id from Quiz q");
        List<Long> ids = query.getResultList();

        if(ids.isEmpty()){
            throw new IllegalStateException("No quiz available");
        }

        return  ids.get(random.nextInt(ids.size()));
    }

    public long createQuiz(
            long subCategoryId,
            String question,
            String firstAnswer,
            String secondAnswer,
            String thirdAnswer,
            String fourthAnswer,
            int indexOfCorrectAnswer
    ){

        SubCategory subCategory = em.find(SubCategory.class, subCategoryId);
        if(subCategory == null){
            throw new IllegalArgumentException("SubCategory "+subCategoryId+" does not exist");
        }

        Quiz quiz = new Quiz();
        quiz.setSubCategoryId(subCategoryId);
        quiz.setQuestion(question);
        quiz.setFirstAnswer(firstAnswer);
        quiz.setSecondAnswer(secondAnswer);
        quiz.setThirdAnswer(thirdAnswer);
        quiz.setFourthAnswer(fourthAnswer);
        quiz.setIndexOfCorrectAnswer(indexOfCorrectAnswer);

        em.persist(quiz);

        return quiz.getId();
    }

    public Void updateQuiz(
            long quizId,
            long subCategoryId,
            String question,
            String firstAnswer,
            String secondAnswer,
            String thirdAnswer,
            String fourthAnswer,
            int indexOfCorrectAnswer
    ){

        Quiz quiz = getQuiz(quizId);
        if(quiz == null){
            throw new IllegalArgumentException("Quiz not found");
        }

        SubCategory subCategory = em.find(SubCategory.class, subCategoryId);
        if(subCategory == null){
            throw new IllegalArgumentException("SubCategory "+subCategoryId+" does not exist");
        }

        quiz.setSubCategoryId(subCategoryId);
        quiz.setQuestion(question);
        quiz.setFirstAnswer(firstAnswer);
        quiz.setSecondAnswer(secondAnswer);
        quiz.setThirdAnswer(thirdAnswer);
        quiz.setFourthAnswer(fourthAnswer);
        quiz.setIndexOfCorrectAnswer(indexOfCorrectAnswer);

        return null;
    }


    public List<Quiz> getQuizzes(){
        Query query = em.createQuery("select q from Quiz q");
        return query.getResultList();
    }

    public List<Quiz> getQuizzes(long subCategoryId){
        Query query = em.createQuery("select q from Quiz q where q.subCategoryId=?1");
        query.setParameter(1, subCategoryId);
        return query.getResultList();
    }


    public Quiz getQuiz(long id){
        return em.find(Quiz.class, id);
    }

    public boolean isPresent(long id){
        return getQuiz(id) != null;
    }

    public Void delete(long id){
        Quiz quiz = getQuiz(id);
        if(quiz != null){
            em.remove(quiz);
        }
        return null;
    }
}
