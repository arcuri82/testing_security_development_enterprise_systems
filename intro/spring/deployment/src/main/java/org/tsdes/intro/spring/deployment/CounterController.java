package org.tsdes.intro.spring.deployment;

import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by arcuri82 on 23-Feb-18.
 */
@Named
@RequestScoped
public class CounterController {

    private static final String COUNTER_NAME = "MAIN_COUNTER";

    @Autowired
    private CounterService service;


    public void increaseCounter(){
        service.increment(COUNTER_NAME);
    }

    public long getCounter(){
        return service.getValueForCounter(COUNTER_NAME);
    }
}
