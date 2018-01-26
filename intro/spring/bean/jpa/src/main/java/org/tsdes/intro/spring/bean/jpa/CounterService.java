package org.tsdes.intro.spring.bean.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 * Created by arcuri82 on 26-Jan-18.
 */
@Service
public class CounterService {

    /*
        Injected EntityManager is thread-safe, so
        not a problem that we use it here in a singleton.

        You might also notice that there is no configuration file for JPA...
        ie no "META-INF/persistence.xml".
        So how is the DB configured?
        If you do not provide any configuration, then SpringBoot
        analyses the classpath (ie third-party libraries), and
        create automated configs for them.
        As in pom.xml we import H2 DB, SpringBoot will automatically
        assume that being the DB we want to use.
     */
    @Autowired
    private EntityManager em;


    public Long getValueForCounter(long id){

        CounterEntity entity = em.find(CounterEntity.class, id);
        if(entity != null){
            return entity.getValue();
        }

        return null;
    }

    @Transactional
    public long createNewCounter(){

        CounterEntity entity = new CounterEntity();
        entity.setValue(0L);

        em.persist(entity);

        return entity.getId();
    }

    public void incrementNotInTransaction(long id){

        CounterEntity entity = em.find(CounterEntity.class, id);
        if(entity == null){
            throw new IllegalArgumentException("No counter exists with id " + id);
        }

        entity.setValue(1L + entity.getValue());
    }


    @Transactional
    public void increment(long id){
        incrementNotInTransaction(id);
    }
}
