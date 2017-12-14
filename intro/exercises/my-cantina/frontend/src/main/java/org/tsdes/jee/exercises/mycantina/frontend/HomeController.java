package org.tsdes.jee.exercises.mycantina.frontend;

import org.tsdes.jee.exercises.mycantina.backend.ejb.MenuEJB;
import org.tsdes.jee.exercises.mycantina.backend.entity.Menu;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by arcuri82 on 30-May-17.
 */
@Named
@SessionScoped
public class HomeController implements Serializable{

    @EJB
    private MenuEJB menuEJB;

    private Menu displayedMenu;

    public Menu getCurrentMenu(){
        if(displayedMenu == null){
            goDefault();
        }

        return displayedMenu;
    }

    public boolean hasCurrent(){
        return getCurrentMenu() != null;
    }

    public void goDefault(){
        displayedMenu = menuEJB.getCurrent();
    }

    public Menu getNext(){
        Menu current = getCurrentMenu();
        if(current == null){
            return null;
        }
        return menuEJB.getNext(current.getDate());
    }

    public Menu getPrevious(){
        Menu current = getCurrentMenu();
        if(current == null){
            return null;
        }
        return menuEJB.getPrevious(current.getDate());
    }

    public void goNext(){
        displayedMenu = getNext();
    }

    public void goPrevious(){
        displayedMenu = getPrevious();
    }
}
