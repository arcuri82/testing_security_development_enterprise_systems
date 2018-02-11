package org.tsdes.intro.spring.security.authorization.po;

import org.tsdes.misc.testutils.selenium.PageObject;

import static org.junit.Assert.assertTrue;

public class ProtectedPO extends PageObject {

    public ProtectedPO(PageObject other) {
        super(other);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Protected resource");
    }

    public IndexPO doLogout(){

        clickAndWait("logoutBtnId");
        IndexPO po = new IndexPO(this);
        assertTrue(po.isOnPage());

        return po;
    }
}
