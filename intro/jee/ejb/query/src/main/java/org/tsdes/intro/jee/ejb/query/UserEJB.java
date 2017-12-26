package org.tsdes.intro.jee.ejb.query;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class UserEJB {

    @PersistenceContext
    private EntityManager em;


    public long createUser(String name) {
        User user = new User();
        user.setName(name);

        em.persist(user);

        return user.getId();
    }

    public void createPost(long userId, String text) {
        Post post = new Post();
        post.setText(text);
        em.persist(post);

        User user = em.find(User.class, userId);
        user.getPosts().add(post);

        updateCounter(user);
    }

    public void createComment(long userId, String text) {
        Comment comment = new Comment();
        comment.setText(text);
        em.persist(comment);

        User user = em.find(User.class, userId);
        user.getComments().add(comment);

        updateCounter(user);
    }

    private void updateCounter(User user) {
        user.setCounter(user.getPosts().size() + user.getComments().size());
    }


    public List<User> getTopUsersUsingCounter(int n) {

        TypedQuery<User> query = em.createQuery("select u from User u order by u.counter DESC", User.class);
        query.setMaxResults(n);

        return query.getResultList();
    }

    public List<User> getTopUsersWithoutCounter(int n) {

        TypedQuery<User> query = em.createQuery(
                "select u from User u order by size(u.posts) + size(u.comments) DESC",
                User.class);
        query.setMaxResults(n);

        return query.getResultList();
    }
}
