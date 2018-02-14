package org.tsdes.intro.spring.testing.mocking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 * Created by arcuri82 on 14-Feb-18.
 */
@Service
@Transactional
public class ServiceA {

    //no annotation here
    private final ServiceB serviceB;

    /*
        Here we do "constructor" injection instead of
        "field" injection
     */

    public ServiceA(@Autowired ServiceB serviceB) {
        this.serviceB = serviceB;
    }

    public String check(long id){

        boolean ok = serviceB.isOK(id);

        if(ok){
            return "OK";
        } else {
            return "NOT OK";
        }
    }
}
