package org.tsdes.jee.exercises.mycantina.backend.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by arcuri82 on 22-May-17.
 */
@Entity
public class Menu {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    @NotNull
    private LocalDate date;

    @NotNull @Size(min = 1)
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Dish> dishes = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }
}
