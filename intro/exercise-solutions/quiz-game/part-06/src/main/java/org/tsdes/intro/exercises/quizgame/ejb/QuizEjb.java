package org.tsdes.intro.exercises.quizgame.ejb;

import org.tsdes.intro.exercises.quizgame.entity.Quiz;
import org.tsdes.intro.exercises.quizgame.entity.SubCategory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Stateless
public class QuizEjb {

    @PersistenceContext
    private EntityManager em;

    /*
     Note that here we return "long" and not "Long".
     "long" is a primitive type, whereas "Long" is an object wrapper.
     This latter can be "null", whereas the former cannot.
     In this particular case, when the method creates a quiz,
     then such a quiz should exist, so the id shouldn't be null, and so "long" type.
     On the other hand, a null when using "Long" could mean a failure in creating the quiz.
     But, in this case, if there is a problem, then an exception would be thrown,
     either by throwing directly a IllegalArgumentException, or by the container
     when the transaction fails.
     Therefore, in such case, there is no point to have the method having the possibility
     to return null.
     */
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

    public List<Quiz> getQuizzes(){
        TypedQuery<Quiz> query = em.createQuery("select q from Quiz q", Quiz.class);
        return query.getResultList();
    }

    public Quiz getQuiz(long id){
        return em.find(Quiz.class, id);
    }

    public List<Quiz> getRandomQuizzes(int n, long categoryId){

        /*
            There can be different ways to sample N rows at random from a table.
            Differences are on performance, based on expected size of the table,
            and the average N values.
            Following approach is "fine" for large tables and low N.

            The idea is that we first make a query to know how many R rows there
            are in the table.
            Then, we make N SQL selects, each one retrieving one row. Rows are
            selected at random, with no replacement.
            When we make a JPQL command, we can specify to get only a subsets of the
            rows, starting at a index K, retrieving Z elements starting from such index K.
            So, here K is at random, and Z=1.
            This process is repeated N times.
            Note that this can end up in 1+N SQL queries. However, it does not require
            any sorting on the table, which would have a O(R*log(R)) complexity.

            However, there is the possibility that a repeated SELECT on same data does not return
            the same ordering (there is no guarantee), unless we explicitly set it with ORDER BY.
            But we could handle the rare case of conflicts (eg 2 different indices resulting by chance in the
            same data because ordering was different) here in the code (which likely would be cheaper
            than forcing a sorting).
         */

        TypedQuery<Long> sizeQuery= em.createQuery(
                "select count(q) from Quiz q where q.subCategory.parent.id=?1", Long.class);
        sizeQuery.setParameter(1, categoryId);
        long size = sizeQuery.getSingleResult();

        if(n > size){
            throw new IllegalArgumentException("Cannot choose " + n + " unique quizzes out of the " + size + " existing");
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

}
