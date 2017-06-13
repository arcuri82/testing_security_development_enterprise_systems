package org.tsdes.jee.jpa.attribute;


import javax.persistence.*;
import java.util.Date;

@Entity
public class Song {

    private String title;
    private String author;

    /*
        When a Song is loaded from the database, a lazy fetch means that "data" is not
        going to be loaded, unless user is going to access it, eg getData().
        Why using it? Performance.
        If I am only interested in the title/author, I do not want to load 4MB of MP3 data
        from the database...
      */
    @Basic(fetch = FetchType.LAZY)
    @Lob //needed for 'L'arge 'ob'jects
    private byte[] data;

    /*
        Each enumeration value has an integer representation, starting from 0,
        representing its ordinal position in the enum definition (eg WAV=0, MP3=1).
        By default, JPA uses those integers. It is more efficient (ie less space),
        but what if a new element is added in the enum definition? What if the
        database is going to be accessed by other systems as well?
        So, might be worthy to store the actual string representation
     */
    @Enumerated(EnumType.STRING)
    private MusicFormat format;


    /*
        Time values are handled specially.
        We need to specify if we are interested in just the date (eg 29/08/2016)
        or in the exact timestamp (eg including seconds)
     */
    @Temporal(TemporalType.DATE)
    private Date publishDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timeEnteredInTheSystem;


    /*
        If for any reason a field should not be mapped to a column in the database,
        then we can use @Transient.
     */
    @Transient
    private Object somethingWeDoNotWantToStoreInTheDatabase;
}
