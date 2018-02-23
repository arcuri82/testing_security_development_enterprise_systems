package org.tsdes.intro.spring.deployment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 * Created by arcuri82 on 26-Jan-18.
 */
@Service
public class CounterService {


    @Autowired
    private EntityManager em;


    public Long getValueForCounter(String id){

        CounterEntity entity = em.find(CounterEntity.class, id);
        if(entity != null){
            return entity.getValue();
        }

        return null;
    }


    @Transactional
    public void createNewCounter(String name){

        CounterEntity entity = new CounterEntity();
        entity.setId(name);
        entity.setValue(0L);

        em.persist(entity);
    }

    @Transactional
    public void increment(String id){

        CounterEntity entity = em.find(CounterEntity.class, id);
        if(entity == null){
            createNewCounter(id);
        }

        entity.setValue(1L + entity.getValue());
    }

}
