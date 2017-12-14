package org.tsdes.intro.jee.jsf.examples.ex01;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named // needed to be accessible from the JSF xhtml files
@SessionScoped  // tells for how long this bean should live, eg the whole user session in this case
public class CounterBean implements Serializable{

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
