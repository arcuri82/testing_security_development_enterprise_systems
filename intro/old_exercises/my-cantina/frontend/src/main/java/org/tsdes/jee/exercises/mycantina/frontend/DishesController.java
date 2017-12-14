package org.tsdes.jee.exercises.mycantina.frontend;

import org.tsdes.jee.exercises.mycantina.backend.ejb.DishEJB;
import org.tsdes.jee.exercises.mycantina.backend.entity.Dish;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.List;

/**
 * Created by arcuri82 on 23-May-17.
 */
@Named
@RequestScoped
public class DishesController {

    @EJB
    private DishEJB dishEJB;

    private String formName;

    private String formDescription;

    public void createDish(){
        dishEJB.createDish(formName, formDescription);
    }

    public List<Dish> getDishes(){
        return dishEJB.getAllDishes();
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormDescription() {
        return formDescription;
    }

    public void setFormDescription(String formDescription) {
        this.formDescription = formDescription;
    }

}
