package org.tsdes.intro.jee.ejb.arquillian;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
public class A {

    @PersistenceContext
    private EntityManager em;

    private int x;

    public A(){
        this.x = 0;
    }

    public int getInternalX(){
        return x;
    }

    public void incrementInternalX(){
        x = x + 1;
    }

    public void updateDB(int y){

        Data data = em.find(Data.class, 0L);
        if(data==null){
            data = new Data();
            data.setId(0L);
            em.persist(data);
        }
        data.setValue(y);
    }

    public int readDB(){
        Data data = em.find(Data.class, 0L);
        if(data == null){
            updateDB(0);
            return 0;
        }
        return data.getValue();
    }
}
