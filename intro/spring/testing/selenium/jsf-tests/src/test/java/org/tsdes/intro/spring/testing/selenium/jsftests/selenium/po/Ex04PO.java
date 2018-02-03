package org.tsdes.intro.spring.testing.selenium.jsftests.selenium.po;

import org.openqa.selenium.By;
import org.tsdes.misc.testutils.selenium.PageObject;

public class Ex04PO extends TemplatePO{

    public Ex04PO(PageObject other) {
        super(other);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("SessionScoped vs. RequestScoped");
    }

    // request

    public void clickRequestPlus(){
        clickAndWait("formRequest:requestPlusButtonId");
    }

    public void clickRequestMinus(){
        clickAndWait("formRequest:requestMinusButtonId");
    }

    public void clickRequestReset(){
        clickAndWait("formRequest:requestResetButtonId");
    }

    public int getRequestCounter(){
        return getInteger("formRequest:requestCounterTextId");
    }

    // session

    public void clickSessionPlus(){
        clickAndWait("formSession:sessionPlusButtonId");
    }

    public void clickSessionMinus(){
        clickAndWait("formSession:sessionMinusButtonId");
    }

    public void clickSessionReset(){
        clickAndWait("formSession:sessionResetButtonId");
    }

    public int getSessionCounter(){
        return getInteger("formSession:sessionCounterTextId");
    }
}
