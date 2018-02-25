package org.tsdes.intro.exercises.quizgame.selenium.po.ui;

import org.openqa.selenium.By;
import org.tsdes.intro.exercises.quizgame.selenium.PageObject;
import org.tsdes.intro.exercises.quizgame.selenium.po.LayoutPO;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class MatchPO extends LayoutPO {


    public MatchPO(PageObject other) {
        super(other);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().trim().equalsIgnoreCase("Match");
    }

    public boolean canSelectCategory(){

        return getCategoryIds().size() > 0;
    }

    public List<String> getCategoryIds(){

        return getDriver().findElements(By.xpath("//input[@data-ctgid]"))
                .stream()
                .map(e -> e.getAttribute("data-ctgid"))
                .collect(Collectors.toList());
    }

    public void chooseCategory(String id){

        clickAndWait("ctgBtnId_" + id);
    }

    public boolean isQuestionDisplayed(){
        return getDriver().findElements(By.id("questionId")).size() > 0;
    }

    public int getQuestionCounter(){
        return getInteger("questionCounterId");
    }

    public long getQuizId(){
        String id = getDriver().findElement(By.xpath("//*[@data-quizid]")).getAttribute("data-quizid");
        return Long.parseLong(id);
    }

    public ResultPO answerQuestion(int index){

        clickAndWait("answerId_" + index);

        if(isOnPage()){
            return null;
        }

        ResultPO po = new ResultPO(this);
        assertTrue(po.isOnPage());

        return po;
    }
}
