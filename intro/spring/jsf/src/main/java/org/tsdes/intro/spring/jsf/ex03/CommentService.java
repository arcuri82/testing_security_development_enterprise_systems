package org.tsdes.intro.spring.jsf.ex03;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@Service
@Transactional
public class CommentService {

    @Autowired
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
