package org.tsdes.intro.spring.flyway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@Transactional
public class DbService {

    @Autowired
    private EntityManager em;

    public List<First> getAllFirst(){
        return em.createQuery("select f from First f", First.class).getResultList();
    }

    public List<Second> getAllSecond(){
        return em.createQuery("select s from Second s", Second.class).getResultList();
    }

}
