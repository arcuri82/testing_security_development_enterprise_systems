package org.tsdes.intro.spring.testing.coverage.jacoco.frontend;

import org.openqa.selenium.WebDriver;
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
        toOrigin();
        waitForPageToLoad();
    }


    public void changeData(String value){
        setText("form:text", value);
        clickAndWait("form:modify");
    }
}
