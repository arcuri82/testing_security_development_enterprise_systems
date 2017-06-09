package org.tsdes.jee.exercises.mycantina.frontend.po;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public abstract class PageObject {

    protected final WebDriver driver;

    public PageObject(WebDriver driver) {
        this.driver = driver;
    }

    public abstract boolean isOnPage();

    public HomePageObject toHomePage(){
        clickAndWait("homeLink");

        return new HomePageObject(driver);
    }

    public void clickAndWait(String id){
        WebElement element = driver.findElement(By.id(id));
        element.click();
        waitForPageToLoad();
    }

    public String getText(String id){
        return driver.findElement(By.id(id)).getText();
    }

    public void setText(String id, String text){
        WebElement element = driver.findElement(By.id(id));
        element.clear();
        element.sendKeys(text);
    }

    protected Boolean waitForPageToLoad() {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        WebDriverWait wait = new WebDriverWait(driver, 10); //give up after 10 seconds

        //keep executing the given JS till it returns "true", when page is fully loaded and ready
        return wait.until((ExpectedCondition<Boolean>) input -> {
            String res = jsExecutor.executeScript("return /loaded|complete/.test(document.readyState);").toString();
            return Boolean.parseBoolean(res);
        });
    }
}
