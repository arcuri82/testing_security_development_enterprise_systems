package org.tsdes.intro.exercises.quizgame.frontend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.tsdes.intro.exercises.quizgame.backend.entity.Category;
import org.tsdes.intro.exercises.quizgame.backend.entity.Quiz;
import org.tsdes.intro.exercises.quizgame.backend.service.CategoryService;
import org.tsdes.intro.exercises.quizgame.backend.service.QuizService;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Created by arcuri82 on 06-Dec-17.
 */
@Named
@SessionScoped
public class MatchController implements Serializable{

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private QuizService quizService;

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
        questions = quizService.getRandomQuizzes(5, selectedCategoryId);
    }



    public List<Category> getCategories(){

        return categoryService.getAllCategories(false);
    }
}
