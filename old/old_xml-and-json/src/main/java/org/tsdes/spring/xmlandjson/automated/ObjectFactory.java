
package org.tsdes.spring.xmlandjson.automated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.pg6100.xmlandjson.automated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _TopPosts_QNAME = new QName("", "topPosts");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.pg6100.xmlandjson.automated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TopPosts }
     * 
     */
    public TopPosts createTopPosts() {
        return new TopPosts();
    }

    /**
     * Create an instance of {@link Post }
     * 
     */
    public Post createPost() {
        return new Post();
    }

    /**
     * Create an instance of {@link TopPosts.PostList }
     * 
     */
    public TopPosts.PostList createTopPostsPostList() {
        return new TopPosts.PostList();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TopPosts }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "topPosts")
    public JAXBElement<TopPosts> createTopPosts(TopPosts value) {
        return new JAXBElement<TopPosts>(_TopPosts_QNAME, TopPosts.class, null, value);
    }

}
