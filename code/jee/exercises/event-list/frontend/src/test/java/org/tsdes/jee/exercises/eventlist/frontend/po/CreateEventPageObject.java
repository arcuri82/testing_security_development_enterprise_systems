package org.tsdes.jee.exercises.eventlist.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import static org.junit.Assert.assertEquals;

public class CreateEventPageObject extends PageObject {

    public CreateEventPageObject(WebDriver driver) {
        super(driver);

        assertEquals("Create New Event", driver.getTitle());
    }

    public boolean isOnPage(){
        return driver.getTitle().equals("Create New Event");
    }

    public HomePageObject createEvent(String title, String country, String location, String description){
        setText("createEventForm:title",title);
        setText("createEventForm:location",location);
        setText("createEventForm:description",description);

        new Select(driver.findElement(By.id("createEventForm:country"))).selectByVisibleText(country);

        driver.findElement(By.id("createEventForm:createButton")).click();
        waitForPageToLoad();

        if(isOnPage()){
            return null;
        } else {
            return new HomePageObject(driver);
        }
    }

}
