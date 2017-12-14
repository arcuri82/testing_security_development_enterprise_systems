package org.tsdes.jee.exercises.quizgame.frontend;

import org.tsdes.jee.exercises.quizgame.backend.ejb.CategoryEjb;
import org.tsdes.jee.exercises.quizgame.backend.ejb.QuizEjb;
import org.tsdes.jee.exercises.quizgame.backend.entity.Category;
import org.tsdes.jee.exercises.quizgame.backend.entity.Quiz;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by arcuri82 on 06-Dec-17.
 */
@Named
@SessionScoped
public class GameController implements Serializable{

    @EJB
    private CategoryEjb categoryEjb;

    @EJB
    private QuizEjb quizEjb;

    private boolean gameIsOn = false;
    private Long selectedCategoryId;
    private int counter;
    private List<Quiz> questions;

    public boolean isGameOn() {
        return gameIsOn;
    }

    public void newGame(){

        if(gameIsOn){
            //TODO lose previous game
        }

        selectedCategoryId = null;
    }

    public boolean isCategorySelected(){
        return selectedCategoryId != null;
    }

    public void selectCategory(long id){
        selectedCategoryId = id;
        counter = 0;
        questions = quizEjb.getRandomQuizzes(5, selectedCategoryId);
    }



    public List<Category> getCategories(){

        return categoryEjb.getAllCategories(false);
    }
}
