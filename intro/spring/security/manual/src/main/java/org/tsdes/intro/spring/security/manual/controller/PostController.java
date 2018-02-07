package org.tsdes.intro.spring.security.manual.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.tsdes.intro.spring.security.manual.entity.Post;
import org.tsdes.intro.spring.security.manual.service.PostService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class PostController implements Serializable{

    @Autowired
    private PostService postService;

    private String formText;


    public String doPostText(String formAuthor){

        boolean posted = postService.post(formText,formAuthor);

        if(posted){
            formText = "";
        }

        return "index.jsf";
    }

    public String getFormText() {
        return formText;
    }

    public void setFormText(String formText) {
        this.formText = formText;
    }

    public String deletePost(long id){
        postService.deletePost(id);
        return "index.jsf";
    }

    public List<Post> getAllPosts(){
        return postService.getAllPosts();
    }
}
