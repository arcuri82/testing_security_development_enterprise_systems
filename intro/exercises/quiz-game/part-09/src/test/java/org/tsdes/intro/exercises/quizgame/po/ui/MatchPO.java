package org.tsdes.intro.exercises.quizgame.po.ui;

import org.openqa.selenium.By;
import org.tsdes.misc.testutils.selenium.PageObject;

public class MatchPO extends PageObject{


    public MatchPO(PageObject other) {
        super(other);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().trim().equalsIgnoreCase("Match");
    }

    public boolean canSelectCategory(){

        return getDriver().findElements(By.id("selectCategoryHeaderId")).size() > 0;
    }
}
