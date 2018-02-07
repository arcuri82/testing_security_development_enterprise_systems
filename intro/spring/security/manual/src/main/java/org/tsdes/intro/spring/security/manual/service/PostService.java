package org.tsdes.intro.spring.security.manual.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tsdes.intro.spring.security.manual.entity.Post;
import org.tsdes.intro.spring.security.manual.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.List;


@Service
@Transactional
public class PostService {

    @Autowired
    private UserService userService;

    @PersistenceContext
    private EntityManager em;


    public boolean post(String text, String author) {

        if (text == null || text.isEmpty()) {
            return false;
        }

        try {
            Post post = new Post();
            post.setCreationTime(ZonedDateTime.now());
            post.setText(text);
            User userDetails = userService.getUser(author);
            post.setAuthor(userDetails);

            em.persist(post);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public List<Post> getAllPosts() {
        TypedQuery<Post> query = em.createQuery(
                "SELECT p FROM Post p ORDER BY p.creationTime DESC", Post.class);
        return query.getResultList();
    }

    public void deletePost(long id) {
        Query query = em.createQuery("DELETE FROM Post p WHERE p.id=:id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
