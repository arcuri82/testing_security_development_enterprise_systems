package org.tsdes.intro.spring.security.framework.selenium.po;

import org.tsdes.misc.testutils.selenium.PageObject;

public class LoginPO extends PageObject{

    public LoginPO(PageObject other) {
        super(other);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().trim().equalsIgnoreCase("Login");
    }

    public IndexPO loginUser(String userId, String password) {

        setText("username", userId);
        setText("password", password);
        clickAndWait("submitBtnId");

        IndexPO po = new IndexPO(this);
        if(! po.isOnPage()){
            return null;
        }

        return po;
    }
}
