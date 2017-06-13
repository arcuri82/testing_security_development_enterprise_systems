package org.tsdes.jee.exercises.mynews.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CommentsPageObject extends PageObject {

    public CommentsPageObject(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOnPage() {
        return driver.getTitle().contains("News Details");
    }

    public void createComment(String text){
        setText("createForm:createText", text);
        WebElement button = driver.findElement(By.id("createForm:createButton"));
        button.click();
        waitForPageToLoad();
    }

    public int getNumberOfComments(){
        List<WebElement> elements = driver.findElements(
                By.xpath("//table[@id='commentTable']//tbody//tr/td/a"));

        return elements.size();
    }



    public boolean isModerateVisible(){
        List<WebElement> elements = driver.findElements(
                By.xpath("//table[@id='commentTable']//thead//tr/th[contains(text(), 'Moderation')]"));

        return ! elements.isEmpty();
    }

    public void doModerate(int position, boolean moderate){

        WebElement checkbox = driver.findElement(By.id("commentTable:"+position+":moderateForm:moderate"));
        if(moderate && checkbox.isSelected()){
            return;
        }
        if(!moderate && !checkbox.isSelected()){
            return;
        }
        checkbox.click();
        waitForPageToLoad();
    }
}
