package org.tsdes.intro.spring.security.authorization.selenium.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.tsdes.misc.testutils.selenium.PageObject;

import static org.junit.Assert.assertTrue;

public class IndexPO extends PageObject {

    public IndexPO(PageObject other) {
        super(other);
    }

    public IndexPO(WebDriver driver, String host, int port) {
        super(driver, host, port);
    }


    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Spring Security Example");
    }

    public void toStartingPage() {
        getDriver().get(host + ":" + port);
        waitForPageToLoad();
    }


    public boolean isLoggedIn() {
        return getDriver().findElements(By.id("loginBtnId")).isEmpty()
                && getDriver().findElements(By.id("logoutBtnId")).size() > 0
                && getDriver().findElements(By.id("helloMsgId")).size() > 0;
    }

    public boolean isLoggedOut() {
        return ! isLoggedIn();
    }


    public SignupPO doSignup() {

        clickAndWait("signupBtnId");
        SignupPO po = new SignupPO(this);
        assertTrue(po.isOnPage());

        return po;
    }

    public LoginPO doLogin(){

        clickAndWait("loginBtnId");
        LoginPO po = new LoginPO(this);
        assertTrue(po.isOnPage());

        return po;
    }

    public ProtectedPO goToProtectedPage(){

        if(getDriver().findElements(By.id("protectedLinkId")).isEmpty()){
            return null;
        }

        clickAndWait("protectedLinkId");
        ProtectedPO po = new ProtectedPO(this);
        assertTrue(po.isOnPage());

        return po;
    }

    public void doLogout(){
        clickAndWait("logoutBtnId");
    }
}
