package org.tsdes.jee.jsf.jacoco.backend;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

@Singleton
public class DataEjb {

    @PersistenceContext
    private EntityManager em;



    public void saveData(@NotNull Long id, @NotNull String text){
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

    public String getData(@NotNull Long id){
        Data data = em.find(Data.class, id);
        if(data == null){
            return null;
        }
        return data.getDataValue();
    }
}
