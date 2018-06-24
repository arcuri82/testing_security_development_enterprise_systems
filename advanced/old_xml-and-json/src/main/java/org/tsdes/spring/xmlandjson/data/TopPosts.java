package org.tsdes.spring.xmlandjson.data;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TopPosts {

    @XmlElement(required = true)
    private Date retrievedTime;

    @XmlElementWrapper(name = "postList", required = true)
    @XmlElement(name = "post", required = true)
    private List<Post> posts;


    public void addPost(Post p){
        if(posts == null){
            posts = new ArrayList<>();
        }
        posts.add(p);
    }


    public Date getRetrievedTime() {
        return retrievedTime;
    }

    public void setRetrievedTime(Date retrievedTime) {
        this.retrievedTime = retrievedTime;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }






}
