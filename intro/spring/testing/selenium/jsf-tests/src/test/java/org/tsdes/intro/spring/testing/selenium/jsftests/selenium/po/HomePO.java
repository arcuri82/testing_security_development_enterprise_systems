package org.tsdes.intro.spring.testing.selenium.jsftests.selenium.po;

import org.openqa.selenium.WebDriver;
import org.tsdes.misc.testutils.selenium.PageObject;

import static org.junit.Assert.assertTrue;

/**
 * Created by arcuri82 on 02-Feb-18.
 */
public class HomePO extends PageObject{

    public HomePO(WebDriver driver, String host, int port) {
        super(driver, host, port);
    }

    public void toStartingPage(){
        driver.get(host + ":" + port);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("JSF Examples");
    }


    public Ex01PO toEx01Page(){

        clickAndWait("ex01LinkId");
        Ex01PO po = new Ex01PO(this);
        assertTrue(po.isOnPage());

        return po;
    }

    public Ex03PO toEx03Page(){

        clickAndWait("ex03LinkId");
        Ex03PO po = new Ex03PO(this);
        assertTrue(po.isOnPage());

        return po;
    }
}
