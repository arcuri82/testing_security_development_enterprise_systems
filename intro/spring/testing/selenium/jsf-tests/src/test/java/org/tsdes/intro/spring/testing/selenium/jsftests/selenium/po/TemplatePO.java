package org.tsdes.intro.spring.testing.selenium.jsftests.selenium.po;

import org.openqa.selenium.WebDriver;
import org.tsdes.misc.testutils.selenium.PageObject;

import static org.junit.Assert.assertTrue;

public abstract class TemplatePO extends PageObject{

    public TemplatePO(WebDriver driver, String host, int port) {
        super(driver, host, port);
    }

    public TemplatePO(PageObject other){
        super(other);
    }

    public HomePO clickBackHome(){

        clickAndWait("backHomeId");

        HomePO po = new HomePO(this);
        assertTrue(po.isOnPage());

        return po;
    }
}
