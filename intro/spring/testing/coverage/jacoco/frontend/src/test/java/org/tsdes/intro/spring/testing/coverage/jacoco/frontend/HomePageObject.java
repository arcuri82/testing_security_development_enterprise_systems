package org.tsdes.intro.spring.testing.coverage.jacoco.frontend;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.tsdes.misc.testutils.selenium.PageObject;

public class HomePageObject extends PageObject {


    public HomePageObject(WebDriver driver, String host, int port) {
        super(driver, host, port);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Simple example");
    }


    public void toStartingPage(){
        getDriver().get(host + ":" + port);
        waitForPageToLoad();
    }


    public void changeData(String value){

        setText("form:text", value);
        clickAndWait("form:modify");
    }
}
