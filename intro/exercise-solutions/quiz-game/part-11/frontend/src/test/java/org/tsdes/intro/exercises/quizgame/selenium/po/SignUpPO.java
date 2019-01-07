package org.tsdes.intro.exercises.quizgame.selenium.po;


import org.tsdes.intro.exercises.quizgame.selenium.PageObject;

/**
 * Created by arcuri82 on 12-Feb-18.
 */
public class SignUpPO extends LayoutPO{

    public SignUpPO(PageObject other) {
        super(other);
    }

    @Override
    public boolean isOnPage() {
        return getDriver().getTitle().contains("Sign Up");
    }

    public IndexPO createUser(String userName, String password){

        setText("username", userName);
        setText("password", password);
        clickAndWait("submit");

        IndexPO po = new IndexPO(this);
        if(po.isOnPage()){
            return po;
        }

        return null;
    }
}
