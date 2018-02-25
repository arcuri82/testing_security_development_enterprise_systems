package org.tsdes.intro.exercises.quizgame.selenium.po.ui;

import org.openqa.selenium.By;
import org.tsdes.intro.exercises.quizgame.selenium.PageObject;
import org.tsdes.intro.exercises.quizgame.selenium.po.LayoutPO;

/**
 * Created by arcuri82 on 06-Feb-18.
 */
public class ResultPO extends LayoutPO {

    public ResultPO(PageObject other) {
        super(other);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Match Result");
    }

    public boolean haveWon(){
        return getDriver().findElements(By.id("wonId")).size() > 0;
    }

    public boolean haveLost(){
        return getDriver().findElements(By.id("lostId")).size() > 0;
    }
}
