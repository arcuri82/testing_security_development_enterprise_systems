package org.tsdes.jee.jsf.examples.test;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * See Page Object at:
 *
 * http://martinfowler.com/bliki/PageObject.html
 *
 * The idea is that, for each web page, we have a class having
 * a method for each of the actions you can do on that page.
 * Such object should abstract away the technical details, eg all
 * the involved HTML, JavaScript, id handling, etc
 */
public abstract class PageObject {

    private final WebDriver driver;

    public PageObject(WebDriver driver) {
        this.driver = driver;
    }

    /*
        Is the browser currently on the page represented by this Page Object?
        We need a method to check if we are on the right page, eg to check we were
        not redirected to an error message page, or if we followed the wrong link
     */
    public abstract boolean isOnPage();

    protected WebDriver getDriver(){
        return driver;
    }

    protected String getBaseUrl(){
        return "http://localhost:8080/examples";
    }

    protected Boolean waitForPageToLoad() {
        WebDriverWait wait = new WebDriverWait(driver, 10); //give up after 10 seconds

        //keep executing the given JS till it returns "true", when page is fully loaded and ready
        return wait.until((ExpectedCondition<Boolean>) input -> (
                (JavascriptExecutor) input).executeScript("return document.readyState").equals("complete"));
    }
}
