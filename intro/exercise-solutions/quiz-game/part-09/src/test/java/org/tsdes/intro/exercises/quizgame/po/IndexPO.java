package org.tsdes.intro.exercises.quizgame.po;

import org.openqa.selenium.WebDriver;
import org.tsdes.intro.exercises.quizgame.po.ui.MatchPO;
import org.tsdes.misc.testutils.selenium.PageObject;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class IndexPO extends PageObject {

    public IndexPO(WebDriver driver, String host, int port) {
        super(driver, host, port);
    }

    public void toStartingPage(){
        toOrigin();
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Quiz Game");
    }

    public MatchPO startNewMatch(){

        clickAndWait("newMatchBtnId");
        MatchPO po = new MatchPO(this);
        assertTrue(po.isOnPage());

        return po;
    }
}
