package org.tsdes.jee.exercises.mynews.frontend;

import org.tsdes.jee.exercises.mynews.backend.ejb.PostEJB;
import org.tsdes.jee.exercises.mynews.backend.entity.Post;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;


@Named
@SessionScoped
public class VoteController implements Serializable{

    public enum Vote {For, None, Against}

    @Inject
    private LoggingController loggingController;

    @EJB
    private PostEJB postEJB;

    public void updateVote(long postId, Vote vote){

        String userId = loggingController.getRegisteredUser();
        switch (vote){
            case For:
                postEJB.voteFor(userId, postId);
                break;
            case None:
                postEJB.noVote(userId, postId);
                break;
            case Against:
                postEJB.voteAgainst(userId, postId);
                break;
        }
    }

    public Vote getVote(Post post){
        String userId = loggingController.getRegisteredUser();

        if(post.getVotesFor().contains(userId)){
            return Vote.For;
        } else if(post.getVotesAgainst().contains(userId)){
            return Vote.Against;
        } else {
            return Vote.None;
        }
    }
}
