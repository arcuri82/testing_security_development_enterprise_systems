package org.tsdes.intro.exercises.quizgame.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tsdes.intro.exercises.quizgame.backend.entity.Quiz;
import org.tsdes.intro.exercises.quizgame.backend.entity.SubCategory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;

/**
 * Created by arcuri82 on 14-Dec-17.
 */
@Service
@Transactional
public class QuizService {

    @Autowired
    private EntityManager em;



    public List<Quiz> getRandomQuizzes(int n, long categoryId){

        TypedQuery<Long> sizeQuery= em.createQuery(
                "select count(q) from Quiz q where q.subCategory.parent.id=?1", Long.class);
        sizeQuery.setParameter(1, categoryId);
        long size = sizeQuery.getSingleResult();

        if(n > size){
            throw new IllegalArgumentException("Cannot chose " + n + " unique quizzes out of the " + size + " existing");
        }

        Random random = new Random();

        List<Quiz> quizzes = new ArrayList<>();
        Set<Integer> chosen = new HashSet<>();

        while(chosen.size() < n) {

            int k = random.nextInt((int)size);
            if(chosen.contains(k)){
                continue;
            }
            chosen.add(k);

            TypedQuery<Quiz> query = em.createQuery(
                    "select q from Quiz q where q.subCategory.parent.id=?1", Quiz.class);
            query.setParameter(1, categoryId);
            query.setMaxResults(1);
            query.setFirstResult(k);

            quizzes.add(query.getSingleResult());
        }


        return  quizzes;
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
        quiz.setSubCategory(subCategory);
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

        quiz.setSubCategory(subCategory);
        quiz.setQuestion(question);
        quiz.setFirstAnswer(firstAnswer);
        quiz.setSecondAnswer(secondAnswer);
        quiz.setThirdAnswer(thirdAnswer);
        quiz.setFourthAnswer(fourthAnswer);
        quiz.setIndexOfCorrectAnswer(indexOfCorrectAnswer);

        return null;
    }


    public List<Quiz> getQuizzes(){
        TypedQuery<Quiz> query = em.createQuery("select q from Quiz q", Quiz.class);
        return query.getResultList();
    }

    public List<Quiz> getQuizzes(long categoryId){
        TypedQuery<Quiz> query = em.createQuery(
                "select q from Quiz q where q.subCategory.parent.id=?1", Quiz.class);
        query.setParameter(1, categoryId);
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
