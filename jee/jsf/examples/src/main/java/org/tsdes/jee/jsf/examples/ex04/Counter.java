package org.tsdes.jee.jsf.examples.ex04;

import java.io.Serializable;

public class Counter implements Serializable {

    private int counter;

    public int getCounter(){
        return counter;
    }

    public void increaseCounter(){
        counter++;
    }

    public void decreaseCounter(){
        if(counter > 0){
            counter--;
        }
    }

    public void reset(){
        counter = 0;
    }
}
