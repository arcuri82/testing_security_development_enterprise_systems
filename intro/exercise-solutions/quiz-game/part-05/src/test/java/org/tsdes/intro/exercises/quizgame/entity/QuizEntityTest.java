package org.tsdes.intro.exercises.quizgame.entity;


import org.junit.jupiter.api.Test;

import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class QuizEntityTest extends EntityTestBase {

    @Test
    public void testQuiz(){

        Quiz quiz = new Quiz();
        quiz.setQuestion("Will this test pass?");
        quiz.setFirstAnswer("Yes");
        quiz.setSecondAnswer("No");
        quiz.setThirdAnswer("Maybe");
        quiz.setFourthAnswer("No idea");
        quiz.setIndexOfCorrectAnswer(0);


        boolean persisted = persistInATransaction(quiz);
        //missing sub-category
        assertFalse(persisted);
    }

    @Test
    public void testQuizWithSubcategory(){

        Category category = new Category();
        category.setName("Enterprise 1");

        SubCategory subCategory = new SubCategory();
        subCategory.setName("JPA");

        category.getSubCategories().add(subCategory);
        subCategory.setParent(category);

        Quiz quiz = new Quiz();
        quiz.setQuestion("Will this test pass?");
        quiz.setFirstAnswer("Yes");
        quiz.setSecondAnswer("No");
        quiz.setThirdAnswer("Maybe");
        quiz.setFourthAnswer("No idea");
        quiz.setIndexOfCorrectAnswer(0);

        quiz.setSubCategory(subCategory);

        assertTrue(persistInATransaction(category,subCategory,quiz));
    }

    private SubCategory addSubcategory(Category category, String subcategoryName){
        SubCategory subCategory = new SubCategory();
        subCategory.setName(subcategoryName);

        category.getSubCategories().add(subCategory);
        subCategory.setParent(category);

        return subCategory;
    }

    private Quiz createQuiz(SubCategory subCategory, String question){

        Quiz quiz = new Quiz();
        quiz.setQuestion(question);
        quiz.setFirstAnswer("a");
        quiz.setSecondAnswer("b");
        quiz.setThirdAnswer("c");
        quiz.setFourthAnswer("d");
        quiz.setIndexOfCorrectAnswer(0);

        quiz.setSubCategory(subCategory);

        return quiz;
    }

    @Test
    public void testQueries(){

        Category jee = new Category();
        jee.setName("JEE");

        SubCategory jpa = addSubcategory(jee, "JPA");
        SubCategory ejb = addSubcategory(jee, "EJB");
        SubCategory jsf = addSubcategory(jee, "JSF");

        assertTrue(persistInATransaction(jee, jpa, ejb, jsf));

        Quiz a = createQuiz(jpa,"a");
        Quiz b = createQuiz(jpa,"b");
        Quiz c = createQuiz(ejb,"c");
        Quiz d = createQuiz(jsf,"d");

        assertTrue(persistInATransaction(a,b,c,d));


        TypedQuery<Quiz> queryJPA = em.createQuery(
                "select q from Quiz q where q.subCategory.name='JPA'",Quiz.class);
        List<Quiz> quizJPA = queryJPA.getResultList();
        assertEquals(2, quizJPA.size());
        assertTrue(quizJPA.stream().anyMatch(q -> q.getQuestion().equals("a")));
        assertTrue(quizJPA.stream().anyMatch(q -> q.getQuestion().equals("b")));

        TypedQuery<Quiz> queryJEE = em.createQuery(
                "select q from Quiz q where q.subCategory.parent.name='JEE'",Quiz.class);
        List<Quiz> all = queryJEE.getResultList();
        assertEquals(4, all.size());
    }

}