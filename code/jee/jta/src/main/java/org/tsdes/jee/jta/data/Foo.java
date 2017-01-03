package org.tsdes.jee.jta.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries(value = {
    @NamedQuery(name = Foo.FIND_ALL, query = "SELECT foo FROM Foo foo"),
    @NamedQuery(name = Foo.DELETE_ALL, query = "DELETE FROM Foo")
})
public class Foo {

    public static final String DELETE_ALL = "Foo.delete_all";
    public static final String FIND_ALL = "Foo.find_all";

    @Id
    private String name;

    public Foo(){
    }

    public Foo(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
