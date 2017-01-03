package org.tsdes.jee.jpa.lock;

public interface Counter {

    Long getId();

    int getCounter();

    void setCounter(int counter);

    /*
        Recall: since Java 8, interfaces can have "default"
        implementations, but no state
     */
    default void increment(){
        int x = getCounter();
        setCounter(x + 1);
    }
}
