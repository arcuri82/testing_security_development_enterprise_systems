package org.tsdes.jee.jsf.examples.ex05.controller;


import org.tsdes.jee.jsf.examples.ex05.entity.Post;
import org.tsdes.jee.jsf.examples.ex05.ejb.PostEJB;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class PostController implements Serializable{

    @Inject
    private PostEJB postEJB;

    private String formText;


    public String doPostText(String formAuthor){

        boolean posted = postEJB.post(formText,formAuthor);

        if(posted){
            formText = "";
        }

        return "ex05.jsf";
    }

    public String getFormText() {
        return formText;
    }

    public void setFormText(String formText) {
        this.formText = formText;
    }

    public String deletePost(long id){
        postEJB.delete(id);
        return "ex05.jsf";
    }

    public List<Post> getAllPosts(){
        return postEJB.getAllPosts();
    }
}
