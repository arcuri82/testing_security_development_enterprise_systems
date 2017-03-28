package org.tsdes.jee.exercises.mynews.backend.ejb;

import org.tsdes.jee.exercises.mynews.backend.entity.Comment;
import org.tsdes.jee.exercises.mynews.backend.entity.Post;
import org.tsdes.jee.exercises.mynews.backend.entity.User;

import javax.ejb.Stateless;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Stateless
public class PostEJB {

    private static final String postFetchAll =
            "LEFT JOIN FETCH p.votesFor  " +
                    "LEFT JOIN FETCH p.votesAgainst  " +
                    "LEFT JOIN FETCH p.comments  AS c " +
                    "LEFT JOIN FETCH c.votesFor " +
                    "LEFT JOIN FETCH c.votesAgainst ";

    @PersistenceContext
    protected EntityManager em;

    public long createPost(@NotNull String authorId, @NotNull String text) {

        User author = em.find(User.class, authorId);
        if (author == null) {
            throw new IllegalArgumentException("Non-existing author: " + authorId);
        }

        Post post = new Post();
        post.setAuthor(author);
        post.setCreatedTime(new Date());
        post.setText(text);

        em.persist(post);

        return post.getId();
    }

    public Post getPost(long postId) {

        TypedQuery<Post> query = em.createQuery(
                "select distinct p from Post p " + postFetchAll +
                        "WHERE p.id=?1", Post.class);
        query.setParameter(1, postId);

        return query.getSingleResult();
    }

    public void voteFor(@NotNull String userId, long postId) {

        Post post = em.find(Post.class, postId, LockModeType.PESSIMISTIC_WRITE);
        if (post == null) {
            throw new IllegalArgumentException(post.getClass().getSimpleName() + " does not exist: " + postId);
        }

        if (post.getVotesFor().contains(userId)) {
            throw new IllegalArgumentException("User " + userId + " already voted for this post");
        }

        post.getVotesFor().add(userId);
        post.getVotesAgainst().remove(userId);
    }

    public void voteAgainst(@NotNull String userId, long postId) {

        Post post = em.find(Post.class, postId, LockModeType.PESSIMISTIC_WRITE);
        if (post == null) {
            throw new IllegalArgumentException("Post does not exist: " + postId);
        }

        if (post.getVotesAgainst().contains(userId)) {
            throw new IllegalArgumentException("User " + userId + " already voted against this post");
        }

        post.getVotesAgainst().add(userId);
        post.getVotesFor().remove(userId);
    }

    public void noVote(@NotNull String userId, long postId) {

        Post post = em.find(Post.class, postId, LockModeType.PESSIMISTIC_WRITE);
        if (post == null) {
            throw new IllegalArgumentException("Post does not exist: " + postId);
        }

        post.getVotesAgainst().remove(userId);
        post.getVotesFor().remove(userId);
    }


    public List<Post> getAllPostsByTime() {
        Query query = em.createQuery("select distinct p from Post p " + postFetchAll + " where TYPE(p) <> Comment order by p.createdTime desc ");
        return query.getResultList();
    }

    public List<Post> getAllPostsByScore() {
        Query query = em.createQuery("select distinct p from Post p  " + postFetchAll + " where TYPE(p) <> Comment");
        List<Post> list = query.getResultList();

        list.sort(Comparator.comparingInt(Post::computeScore).reversed());

        return list;
    }

    //------------- comments --------------


    public long createComment(@NotNull String authorId, long postId, @NotNull String text) {

        User author = em.find(User.class, authorId);
        if (author == null) {
            throw new IllegalArgumentException("Non-existing author: " + authorId);
        }

        Post post = em.find(Post.class, postId, LockModeType.PESSIMISTIC_WRITE);
        if (post == null) {
            throw new IllegalArgumentException("Non-existing post: " + postId);
        }

        Comment comment = new Comment();
        comment.setText(text);
        comment.setCreatedTime(new Date());
        comment.setAuthor(author);
        comment.setModerated(false);
        comment.setPost(post);

        em.persist(comment);

        post.getComments().add(comment);

        return comment.getId();
    }

    public void moderate(@NotNull String userId, long commentId, boolean moderated) {
        Comment comment = em.find(Comment.class, commentId);
        if (comment == null) {
            throw new IllegalArgumentException("Comment does not exist");
        }

        if (!userId.equals(comment.getPost().getAuthor().getUserId())) {
            throw new IllegalArgumentException("User " + userId + " is not allowed to moderate comment " + commentId);
        }

        comment.setModerated(moderated);
    }
}
