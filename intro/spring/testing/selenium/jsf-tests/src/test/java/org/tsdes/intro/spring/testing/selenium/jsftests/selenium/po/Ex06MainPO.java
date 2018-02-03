package org.tsdes.intro.spring.testing.selenium.jsftests.selenium.po;

import org.tsdes.misc.testutils.selenium.PageObject;

import static org.junit.Assert.assertTrue;

public class Ex06MainPO extends TemplatePO {

    public Ex06MainPO(PageObject other) {
        super(other);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Example of Redirections");
    }


    public Ex06ResultPO clickForward(){
        clickAndWait("formSession:forwardBtnId");

        Ex06ResultPO po = new Ex06ResultPO(this);
        assertTrue(po.isOnPage());

        return po;
    }

    public Ex06ResultPO clickRedirect(){
        clickAndWait("formSession:redirectBtnId");

        Ex06ResultPO po = new Ex06ResultPO(this);
        assertTrue(po.isOnPage());

        return po;
    }

}
