package org.tsdes.jee.exercises.eventlist.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;


public class HomePageObject extends PageObject {


    public HomePageObject(WebDriver driver) {
        super(driver);
    }

    public HomePageObject toStartingPage() {
        String context = "/exam_example"; // see jboss-web.xml
        driver.get("localhost:8080" + context + "/home.jsf");
        waitForPageToLoad();

        return this;
    }

    public boolean isOnPage() {
        return driver.getTitle().equals("Event List Home Page");
    }

    public LoginPageObject toLogin() {
        if (isLoggedIn()) {
            logout();
        }

        driver.findElement(By.id("login")).click();
        waitForPageToLoad();
        return new LoginPageObject(driver);
    }


    public CreateEventPageObject toCreateEvent() {
        if (!isLoggedIn()) {
            return null;
        }

        driver.findElement(By.id("createEvent")).click();
        waitForPageToLoad();
        return new CreateEventPageObject(driver);
    }

    public int getNumberOfDisplayedEvents() {
        List<WebElement> elements = driver.findElements(
                By.xpath("//table[@id='eventTable']//tbody//tr[string-length(text()) > 0]"));

        return elements.size();
    }

    public void setShowOnlyOwnCountry(boolean value) {

        List<WebElement> elements = driver.findElements(By.id("showOnlyOwnCountryForm:showOnlyOwnCountry"));
        WebElement e = elements.get(0);

        if ((value && !e.isSelected()) || (!value && e.isSelected())) {
            e.click();
            waitForPageToLoad();
        }
    }


    public int getNumberOfAttendees(String title) {
        List<WebElement> elements = driver.findElements(
                By.xpath("//table[@id='eventTable']//tbody//tr[contains(td[2], '" + title + "')]/td[4]")
        );
        if (elements.isEmpty()) {
            return -1;
        }
        return Integer.parseInt(elements.get(0).getText());
    }


    public boolean isAttending(String title) {
        List<WebElement> elements = driver.findElements(
                By.xpath("//table[@id='eventTable']//tbody//tr[contains(td[2], '" + title + "')]/td[5]/form/input[@type='checkbox' and @checked='checked']")
        );

        return !elements.isEmpty();
    }

    public void setAttendance(String title, boolean value) {
        boolean alreadyAttending = isAttending(title);
        if ((value && alreadyAttending) || (!value && !alreadyAttending)) {
            return;
        }

        List<WebElement> elements = driver.findElements(
                By.xpath("//table[@id='eventTable']//tbody//tr[contains(td[2], '" + title + "')]/td[5]/form/input[@type='checkbox']")
        );
        if (elements.isEmpty()) {
            return;
        }

        elements.get(0).click();
        waitForPageToLoad();
    }
}
