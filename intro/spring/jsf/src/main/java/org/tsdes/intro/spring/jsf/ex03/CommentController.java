package org.tsdes.intro.spring.jsf.ex03;


import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.List;

@Named //need to be accessible from JSF XHTML files
@RequestScoped //no need to keep state, just needed for the POST request
public class CommentController {

    private String formText;

    @Autowired
    private CommentService service;

    public void createNewComment() {
        service.createNewComment(formText);
        formText = "";
    }

    public List<Comment> getMostRecentComments(int max) {
        return service.getMostRecentComments(max);
    }

    public void deleteComment(Long id) {
        service.deleteComment(id);
    }

    public String getFormText() {
        return formText;
    }

    public void setFormText(String formText) {
        this.formText = formText;
    }
}
