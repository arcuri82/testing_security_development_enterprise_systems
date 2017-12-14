package org.tsdes.jee.exercises.mynews.frontend;


import org.tsdes.jee.exercises.mynews.backend.ejb.PostEJB;
import org.tsdes.jee.exercises.mynews.backend.entity.Comment;
import org.tsdes.jee.exercises.mynews.backend.entity.Post;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIInput;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Named
@SessionScoped
public class CommentController implements Serializable{

    @EJB
    private PostEJB postEJB;

    @Inject
    private LoggingController loggingController;

    @Inject
    private VoteController voteController;

    private long postId;


    private String formText;

    private Map<Long, VoteController.Vote> voteMap;

    public String openCommentsPage(long postId){
        this.postId = postId;
        return "comments.jsf";
    }


    public void createNewComment(long postId){

        postEJB.createComment(loggingController.getRegisteredUser(), postId, formText);
    }

    public Post getPost(long postId){
        return postEJB.getPost(postId);
    }

    public List<Comment> getComments(long postId){

        Post post = getPost(postId);

        voteMap = new ConcurrentHashMap<>();

        List<Comment> comments = post.getComments();

        comments.stream().forEach(c ->
                voteMap.put(c.getId(), voteController.getVote(c))
        );

        return comments;
    }

    public void updateVoteListener(ValueChangeEvent event){

        Long commentId =(Long) ((UIInput) event.getSource()).getAttributes().get("commentId");
        VoteController.Vote vote = VoteController.Vote.valueOf(event.getNewValue().toString());

        voteController.updateVote(commentId, vote);
    }

    public String getCommentText(Comment comment){

        if(comment.getModerated()){
            return "This comment has been moderated";
        } else {
            return comment.getText();
        }
    }

    public void flipModerate(Comment comment){
        boolean flipped = ! comment.getModerated();
        postEJB.moderate(loggingController.getRegisteredUser(), comment.getId(), flipped);
    }

    public String getFormText() {
        return formText;
    }

    public void setFormText(String formText) {
        this.formText = formText;
    }

    public Map<Long, VoteController.Vote> getVoteMap() {
        return voteMap;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }
}
