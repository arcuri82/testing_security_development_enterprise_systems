package org.tsdes.intro.spring.testing.selenium.jsftests.selenium.po;

import org.tsdes.misc.testutils.selenium.PageObject;

import static org.junit.Assert.assertTrue;

public class Ex05ParamsPO extends TemplatePO {

    public Ex05ParamsPO(PageObject other) {
        super(other);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Page using URL Parameters");
    }

    public int getCounterOfParams(){
        return getInteger("paramCounterId");
    }

    public Ex05MainPO clickOnBackToMain(){
        clickAndWait("linkToMainId");

        Ex05MainPO po = new Ex05MainPO(this);
        assertTrue(po.isOnPage());

        return po;
    }

}
