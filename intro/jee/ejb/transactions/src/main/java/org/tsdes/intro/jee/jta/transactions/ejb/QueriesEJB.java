package org.tsdes.intro.jee.jta.transactions.ejb;

import org.tsdes.intro.jee.jta.transactions.data.Foo;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class QueriesEJB {

    @PersistenceContext
    private EntityManager em;

    public int deleteAll(){
        return em.createQuery("DELETE FROM Foo").executeUpdate();
    }

    public boolean isInDB(String name){
        return em.find(Foo.class, name) != null;
    }
}
