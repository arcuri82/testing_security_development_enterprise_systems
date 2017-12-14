package org.tsdes.intro.jee.jpa.table;


import javax.persistence.*;


/*
    there is no table called "MovieDetails", so need to specify
    to which existing table this Entity should be mapped to
 */
@Table(name = "MOVIE")
@Entity
public class MovieDetails {

    @Column(name = "ID") @Id
    private Long id;

    private String title; //same column name

    //No column called "directorName", so we map it to the existing "DIRECTOR"
    @Column(name = "DIRECTOR")
    private String directorName;


    public MovieDetails(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirectorName() {
        return directorName;
    }

    public void setDirectorName(String directorName) {
        this.directorName = directorName;
    }
}
