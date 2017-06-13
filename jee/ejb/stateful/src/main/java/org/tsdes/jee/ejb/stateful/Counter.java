package org.tsdes.jee.ejb.stateful;

import java.io.Serializable;

public abstract class Counter implements Serializable{

    private int x;

    protected Counter(){
        x = 0;
    }

    public void increment(){
        x = x + 1;
    }

    public int get(){
        return x;
    }
}
