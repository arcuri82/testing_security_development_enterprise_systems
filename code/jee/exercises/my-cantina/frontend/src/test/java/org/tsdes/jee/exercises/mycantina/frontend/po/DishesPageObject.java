package org.tsdes.jee.exercises.mycantina.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by arcuri82 on 31-May-17.
 */
public class DishesPageObject extends PageObject {


    public DishesPageObject(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOnPage() {
        return driver.getTitle().equals("Dishes");
    }

    public void createDish(String name, String description){
        setText("createForm:formName", name);
        setText("createForm:formDescription", description);

        clickAndWait("createForm:createButton");
    }

    public boolean hasDishByName(String name){
        List<WebElement> elements = driver.findElements(
                By.xpath("//label[@class='dishName' and contains(text(),'"+name+"')]"));
        return ! elements.isEmpty();
    }


}
