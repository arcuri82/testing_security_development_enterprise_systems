package org.tsdes.intro.spring.security.manual.selenium.po;

import org.tsdes.misc.testutils.selenium.PageObject;

public class LoginPO extends PageObject {


    public LoginPO(PageObject other) {
        super(other);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Login / Create New User");
    }

    public IndexPO createUser(String userId, String password){

        setText("loginForm:userId", userId);
        setText("loginForm:password", password);
        clickAndWait("loginForm:create");

        IndexPO po = new IndexPO(this);
        return po;
    }
}
