package org.tsdes.intro.spring.testing.mocking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 * Created by arcuri82 on 14-Feb-18.
 */
@Service
public class ServiceB {

    //no annotation here
    private final EntityManager em;

    /*
       Here we do "constructor" injection instead of
       "field" injection
     */

    public ServiceB(@Autowired EntityManager em) {
        this.em = em;
    }

    public boolean isOK(long id){

        FooEntity entity = em.find(FooEntity.class, id);
        if(entity == null){
            return false;
        }

        return entity.getOK();
    }

    @Transactional
    public long createEntry(boolean ok){

        FooEntity entity = new FooEntity();
        entity.setOK(ok);

        em.persist(entity);

        return entity.getId();
    }
}
