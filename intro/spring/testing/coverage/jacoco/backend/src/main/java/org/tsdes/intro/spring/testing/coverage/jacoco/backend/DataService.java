package org.tsdes.intro.spring.testing.coverage.jacoco.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;

@Service
public class DataService {

    @Autowired
    private EntityManager em;


    @Transactional
    public void saveData(Long id, String text){
        Data data = em.find(Data.class, id);
        if(data == null){
            data = new Data();
            data.setId(id);
            data.setDataValue(text);
            em.persist(data);
        } else {
            data.setDataValue(text);
        }
    }

    public String getData(Long id){
        Data data = em.find(Data.class, id);
        if(data == null){
            return null;
        }
        return data.getDataValue();
    }
}
