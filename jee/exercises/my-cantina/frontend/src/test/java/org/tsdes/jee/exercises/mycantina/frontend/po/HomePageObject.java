package org.tsdes.jee.exercises.mycantina.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;


public class HomePageObject extends PageObject {



    public HomePageObject(WebDriver driver, String host, int port) {
        super(driver, host, port);
    }

    public HomePageObject toStartingPage() {
        String context = "/my_cantina"; // see jboss-web.xml
        driver.get("http://"+host+":"+ port + context + "/home.jsf");
        waitForPageToLoad();

        return this; //driver.get("http://172.17.0.2:8080")
    }

    public boolean isOnPage() {
        return driver.getTitle().equals("MyCantina Home Page");
    }

    public DishesPageObject toDishes() {
        clickAndWait("dishesLink");
        DishesPageObject po = new DishesPageObject(driver, host, port);
        assertTrue(po.isOnPage());
        return po;
    }

    public MenuPageObject toMenu() {
        clickAndWait("menuLink");
        MenuPageObject po = new MenuPageObject(driver, host, port);
        assertTrue(po.isOnPage());
        return po;
    }

    public void displayDefault() {
        clickAndWait("def:defaultBtn");
    }

    public LocalDate getDisplayedDate() {
        return LocalDate.parse(getText("displayedDate"));
    }

    public List<String> getDisplayedDishNames() {

        return driver.findElements(By.xpath("//label[@class='dishName']")).stream()
                .map(e -> e.getText())
                .collect(Collectors.toList());
    }

    public void clickNext(){
        clickAndWait("next:nextBtn");
    }

    public void clickPrevious(){
        clickAndWait("previous:previousBtn");
    }
}
