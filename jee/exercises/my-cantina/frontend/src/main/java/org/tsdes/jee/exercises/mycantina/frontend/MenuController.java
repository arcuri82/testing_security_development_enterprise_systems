package org.tsdes.jee.exercises.mycantina.frontend;

import org.tsdes.jee.exercises.mycantina.backend.ejb.MenuEJB;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by arcuri82 on 30-May-17.
 */
@Named
@RequestScoped
public class MenuController {

    @EJB
    private MenuEJB menuEJB;

    private String dateForm;

    private Map<Long, Boolean> checksForm = new HashMap<>();

    public String createMenu() {

        try {
            LocalDate date = LocalDate.parse(dateForm);

            List<Long> ids = checksForm.entrySet().stream()
                    .filter(e -> e.getValue())
                    .map(e -> e.getKey())
                    .collect(Collectors.toList());

            menuEJB.createMenu(date, ids);

            return "/home.jsf";
        }catch (Exception e) {
            return "/menu.jsf";
        }
    }


    public String getDateForm() {
        return dateForm;
    }

    public void setDateForm(String dateForm) {
        this.dateForm = dateForm;
    }

    public Map<Long, Boolean> getChecksForm() {
        return checksForm;
    }

    public void setChecksForm(Map<Long, Boolean> checksForm) {
        this.checksForm = checksForm;
    }
}
