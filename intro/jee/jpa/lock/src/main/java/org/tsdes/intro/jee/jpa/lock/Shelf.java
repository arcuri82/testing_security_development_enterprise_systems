package org.tsdes.intro.jee.jpa.lock;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Shelf {

    @Id @GeneratedValue
    private Long id;

    @OneToMany
    private List<Book> books;

    public Shelf(){}


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Book> getBooks() {
        if(books == null){
            books = new ArrayList<>();
        }
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
