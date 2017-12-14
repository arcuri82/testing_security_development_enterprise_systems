package org.tsdes.intro.jee.jsf.examples.ex03;


import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.List;

@Named //need to be accessible from JSF XHTML files
@RequestScoped //no need to keep state, just needed for the POST request
public class CommentController {

    private String formText;

    @EJB
    private CommentEJB ejb;

    public void createNewComment() {
        ejb.createNewComment(formText);
        formText = "";
    }

    public List<Comment> getMostRecentComments(int max){
        return ejb.getMostRecentComments(max);
    }

    public void deleteComment(Long id){
        ejb.deleteComment(id);
    }

    public String getFormText() {
        return formText;
    }

    public void setFormText(String formText) {
        this.formText = formText;
    }
}
