package org.tsdes.intro.jee.jsf.examples.ex03;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@Stateless
public class CommentEJB {

    @PersistenceContext
    private EntityManager em;

    public void createNewComment(@NotNull String text) {

        Comment comment = new Comment();
        comment.setText(text);
        comment.setDate(new Date());

        em.persist(comment);
    }

    public List<Comment> getMostRecentComments(int max){

        Query query = em.createQuery("select c from Comment c order by c.date DESC ");
        query.setMaxResults(max);

        return query.getResultList();
    }

    public void deleteComment(long id){
        Comment comment = em.find(Comment.class, id);
        if(comment != null){
            em.remove(comment);
        }
    }
}
