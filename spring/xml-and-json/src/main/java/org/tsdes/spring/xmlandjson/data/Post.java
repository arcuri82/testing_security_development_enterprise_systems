package org.tsdes.spring.xmlandjson.data;

import javax.xml.bind.annotation.*;


@XmlAccessorType(XmlAccessType.FIELD)
public class Post {

    @XmlElement(nillable = false, required = true)
    private String content;

    @XmlAttribute
    private String author;

    @XmlAttribute
    private Integer votes;



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (content != null ? !content.equals(post.content) : post.content != null) return false;
        if (author != null ? !author.equals(post.author) : post.author != null) return false;
        return votes != null ? votes.equals(post.votes) : post.votes == null;

    }

    @Override
    public int hashCode() {
        int result = content != null ? content.hashCode() : 0;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (votes != null ? votes.hashCode() : 0);
        return result;
    }
}
