package org.tsdes.jee.exercises.mynews.frontend;

import org.tsdes.jee.exercises.mynews.backend.ejb.PostEJB;
import org.tsdes.jee.exercises.mynews.backend.entity.Post;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIInput;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Named
@SessionScoped
public class PostController implements Serializable{


    public enum Sorting {Time, Score}

    @Inject
    private LoggingController loggingController;

    @Inject
    private VoteController voteController;

    @EJB
    private PostEJB postEJB;

    private String formText;

    private Sorting sorting = Sorting.Time;

    private Map<Long, VoteController.Vote> voteMap;

    public void createNewPost(){
        postEJB.createPost(loggingController.getRegisteredUser(), formText);
    }


    public List<Post> getPosts(){

        List<Post> posts;

        if(sorting.equals(Sorting.Score)) {
            posts = postEJB.getAllPostsByScore();
        } else {
            posts = postEJB.getAllPostsByTime();
        }

        voteMap = new ConcurrentHashMap<>();
        posts.stream().forEach(p ->
            voteMap.put(p.getId(), voteController.getVote(p))
        );

        return posts;
    }

    public Map<Long, VoteController.Vote> getVoteMap() {
        return voteMap;
    }

    public void updateVoteListener(ValueChangeEvent event){

        Long postId =(Long) ((UIInput) event.getSource()).getAttributes().get("postId");
        VoteController.Vote vote = VoteController.Vote.valueOf(event.getNewValue().toString());

        voteController.updateVote(postId, vote);
    }


    public static String excerpt(String text){
        int limit = 30;

        if(text.length() > limit){
            return text.substring(0, limit - 4) + " ...";
        }
        return text;
    }

    public String getPostExcerpt(Post post){
        String text = post.getText();
        return excerpt(text);
    }

    public String getFormText() {
        return formText;
    }

    public void setFormText(String formText) {
        this.formText = formText;
    }

    public Sorting getSorting() {
        return sorting;
    }

    public void setSorting(Sorting sorting) {
        this.sorting = sorting;
    }

    public List<Sorting> getSortModes(){
        return Arrays.asList(Sorting.Time, Sorting.Score);
    }
}
