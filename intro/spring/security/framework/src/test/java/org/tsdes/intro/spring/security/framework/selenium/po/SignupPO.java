package org.tsdes.intro.spring.security.framework.selenium.po;

import org.tsdes.misc.testutils.selenium.PageObject;

public class SignupPO extends PageObject {


    public SignupPO(PageObject other) {
        super(other);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().trim().equalsIgnoreCase("Sign In");
    }

    public IndexPO createUser(String userId, String password) {

        setText("usernameTextId", userId);
        setText("passwordTextId", password);
        clickAndWait("submitBtnId");

        IndexPO po = new IndexPO(this);
        if(! po.isOnPage()){
            return null;
        }

        return po;
    }
}
