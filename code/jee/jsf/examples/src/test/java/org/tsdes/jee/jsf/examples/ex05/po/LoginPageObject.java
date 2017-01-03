package org.tsdes.jee.jsf.examples.ex05.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.tsdes.jee.jsf.examples.test.PageObject;

public class LoginPageObject extends PageObject{


    public LoginPageObject(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Login / Create New User");
    }

    public HomePageObject createUser(String userId, String password){

        WebElement id = getDriver().findElement(By.id("loginForm:userId"));
        WebElement pwd = getDriver().findElement(By.id("loginForm:password"));
        WebElement create = getDriver().findElement(By.id("loginForm:create"));

        id.clear();
        id.sendKeys(userId);
        pwd.clear();
        pwd.sendKeys(password);
        create.click();
        waitForPageToLoad();

        HomePageObject po = new HomePageObject(getDriver());
        return po;
    }
}
