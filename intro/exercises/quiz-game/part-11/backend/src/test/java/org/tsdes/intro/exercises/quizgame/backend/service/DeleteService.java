package org.tsdes.intro.exercises.quizgame.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tsdes.intro.exercises.quizgame.backend.entity.Quiz;
import org.tsdes.intro.exercises.quizgame.backend.entity.Category;
import org.tsdes.intro.exercises.quizgame.backend.entity.SubCategory;
import org.tsdes.intro.exercises.quizgame.backend.entity.User;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Created by arcuri82 on 14-Dec-17.
 */
@Service
@Transactional
public class DeleteService {

    @PersistenceContext
    private EntityManager em;

    public void deleteAll(){
        deleteEntities(User.class);
        deleteEntities(Quiz.class);
        deleteEntities(SubCategory.class);
        deleteEntities(Category.class);
    }

    private void deleteEntities(Class<?> entity){

        if(entity == null || entity.getAnnotation(Entity.class) == null){
            throw new IllegalArgumentException("Invalid non-entity class");
        }

        String name = entity.getSimpleName();

        /*
            Note: we passed as input a Class<?> instead of a String to
            avoid SQL injection. However, being here just test code, it should
            not be a problem. But, as a good habit, always be paranoiac about
            security, above all when you have code that can delete the whole
            database...
         */

        Query query = em.createQuery("delete from " + name);
        query.executeUpdate();
    }

}
