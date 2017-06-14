
package org.tsdes.spring.xmlandjson.automated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for topPosts complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="topPosts">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="retrievedTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="postList">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="post" type="{}post" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlRootElement //added manually
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "topPosts", propOrder = {
    "retrievedTime",
    "postList"
})
public class TopPosts {

    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar retrievedTime;
    @XmlElement(required = true)
    protected TopPosts.PostList postList;

    /**
     * Gets the value of the retrievedTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRetrievedTime() {
        return retrievedTime;
    }

    /**
     * Sets the value of the retrievedTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRetrievedTime(XMLGregorianCalendar value) {
        this.retrievedTime = value;
    }

    /**
     * Gets the value of the postList property.
     * 
     * @return
     *     possible object is
     *     {@link TopPosts.PostList }
     *     
     */
    public TopPosts.PostList getPostList() {
        return postList;
    }

    /**
     * Sets the value of the postList property.
     * 
     * @param value
     *     allowed object is
     *     {@link TopPosts.PostList }
     *     
     */
    public void setPostList(TopPosts.PostList value) {
        this.postList = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="post" type="{}post" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "post"
    })
    public static class PostList {

        @XmlElement(required = true)
        protected List<Post> post;

        /**
         * Gets the value of the post property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the post property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPost().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link Post }
         * 
         * 
         */
        public List<Post> getPost() {
            if (post == null) {
                post = new ArrayList<Post>();
            }
            return this.post;
        }

    }

}
