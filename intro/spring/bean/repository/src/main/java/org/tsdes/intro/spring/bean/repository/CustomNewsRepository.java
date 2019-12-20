package org.tsdes.intro.spring.bean.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by arcuri82 on 20-Dec-19.
 */
@Repository
public interface CustomNewsRepository extends CrudRepository<NewsEntity, Long>
        /*
            the repository can be extended with custom functionality defined in
            another interface
         */
        , MyCustomMethods {

    List<NewsEntity> findAllByCountry(String country);

    List<NewsEntity> findAllByAuthorId(String authorId);

    List<NewsEntity> findAllByCountryAndAuthorId(String country, String authorId);

    @Query("select n from NewsEntity n where n.country=?1 and n.authorId=?2")
    List<NewsEntity> customQuery(String country, String authorId);
}


/*
    New functionality will need to be defined in an interface, not a class
 */
interface MyCustomMethods{

    @Transactional
    boolean changeText(long newsId, String text);
}


/*
    However, we still must provide an implementation for the interface with the new
    functionality.
    Such class MUST have the same name with ending "Impl", otherwise Spring will
    not recognize it
 */
class MyCustomMethodsImpl implements MyCustomMethods{

    /*
        Such class is a Spring bean, so we can autowire whatever we want
     */
    @Autowired
    private EntityManager em;

    @Override
    public boolean changeText(long newsId, String text) {

        NewsEntity news = em.find(NewsEntity.class, newsId);
        if(news == null){
            return false;
        }

        news.setText(text);
        em.merge(news);

        return true;
    }
}

