package org.tsdes.intro.spring.testing.selenium.jsftests.selenium.po;

import org.tsdes.misc.testutils.selenium.PageObject;

import static org.junit.Assert.assertTrue;

public class Ex06ResultPO extends TemplatePO {

    public Ex06ResultPO(PageObject other) {
        super(other);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Page Showing Redirection");
    }


    public Ex06MainPO clickBackToMain(){
        clickAndWait("linkMainId");

        Ex06MainPO po = new Ex06MainPO(this);
        assertTrue(po.isOnPage());

        return po;
    }


    public int getDisplayedCounter(){
        return getInteger("counterId");
    }
}
