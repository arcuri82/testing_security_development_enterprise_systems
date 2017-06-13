package org.tsdes.jee.ejb.time.businesslayer;

import org.tsdes.jee.ejb.time.datalayer.Comment;
import org.tsdes.jee.ejb.time.datalayer.News;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;


@Stateless
public class CommentEJB {

    @PersistenceContext
    private EntityManager em;


    /**
        Create a comment to the News identified by {@code newsId}
     */
    public void save(@NotNull Long newsId, @NotNull String author, @NotNull String draft){

        News news = em.find(News.class, newsId);
        if(news == null){
            return;
        }

        Comment comment = new Comment(null, draft, author);
        em.persist(comment);

        news.getComments().add(comment);
    }
}
