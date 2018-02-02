package org.tsdes.intro.jee.jsf.examples.ex05.ejb;


import org.tsdes.intro.jee.jsf.examples.ex05.entity.Post;
import org.tsdes.intro.jee.jsf.examples.ex05.entity.UserDetails;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Stateless
public class PostEJB implements Serializable{

    @EJB
    private UserEJB userEJB;

    @PersistenceContext
    private EntityManager em;


    public boolean post(String text, String author) {

        if (text == null || text.isEmpty()) {
            return false;
        }

        try {
            Post post = new Post();
            post.setCreationTime(new Date());
            post.setText(text);
            UserDetails userDetails = userEJB.getUser(author);
            post.setAuthor(userDetails);

            em.persist(post);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public List<Post> getAllPosts() {
        Query query = em.createNamedQuery(Post.GET_ALL);
        return query.getResultList();
    }

    public void delete(long id) {
        Query query = em.createNamedQuery(Post.DELETE_POST);
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
