
package automated.metno;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element ref="{}productdescription"/>
 *         &lt;element ref="{}img" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}title"/>
 *         &lt;element ref="{}productheader"/>
 *         &lt;element ref="{}guielement" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}guilist" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}locationlist" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{}text"/>
 *           &lt;element ref="{}comment"/>
 *           &lt;element ref="{}br"/>
 *           &lt;element ref="{}hr"/>
 *         &lt;/choice>
 *         &lt;element ref="{}time" maxOccurs="unbounded"/>
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
    "productdescription",
    "img",
    "title",
    "productheader",
    "guielement",
    "guilist",
    "locationlist",
    "textOrCommentOrBr",
    "time"
})
@XmlRootElement(name = "weather")
public class Weather {

    @XmlElement(required = true)
    protected String productdescription;
    protected List<Img> img;
    @XmlElement(required = true)
    protected String title;
    @XmlElement(required = true)
    protected Productheader productheader;
    protected List<Guielement> guielement;
    protected List<Guilist> guilist;
    protected List<Locationlist> locationlist;
    @XmlElements({
        @XmlElement(name = "text", type = Text.class),
        @XmlElement(name = "comment", type = String.class),
        @XmlElement(name = "br", type = Br.class),
        @XmlElement(name = "hr", type = Hr.class)
    })
    protected List<Object> textOrCommentOrBr;
    @XmlElement(required = true)
    protected List<Time> time;

    /**
     * Gets the value of the productdescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductdescription() {
        return productdescription;
    }

    /**
     * Sets the value of the productdescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductdescription(String value) {
        this.productdescription = value;
    }

    /**
     * Gets the value of the img property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the img property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImg().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Img }
     * 
     * 
     */
    public List<Img> getImg() {
        if (img == null) {
            img = new ArrayList<Img>();
        }
        return this.img;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the productheader property.
     * 
     * @return
     *     possible object is
     *     {@link Productheader }
     *     
     */
    public Productheader getProductheader() {
        return productheader;
    }

    /**
     * Sets the value of the productheader property.
     * 
     * @param value
     *     allowed object is
     *     {@link Productheader }
     *     
     */
    public void setProductheader(Productheader value) {
        this.productheader = value;
    }

    /**
     * Gets the value of the guielement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the guielement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGuielement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Guielement }
     * 
     * 
     */
    public List<Guielement> getGuielement() {
        if (guielement == null) {
            guielement = new ArrayList<Guielement>();
        }
        return this.guielement;
    }

    /**
     * Gets the value of the guilist property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the guilist property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGuilist().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Guilist }
     * 
     * 
     */
    public List<Guilist> getGuilist() {
        if (guilist == null) {
            guilist = new ArrayList<Guilist>();
        }
        return this.guilist;
    }

    /**
     * Gets the value of the locationlist property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the locationlist property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocationlist().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Locationlist }
     * 
     * 
     */
    public List<Locationlist> getLocationlist() {
        if (locationlist == null) {
            locationlist = new ArrayList<Locationlist>();
        }
        return this.locationlist;
    }

    /**
     * Gets the value of the textOrCommentOrBr property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the textOrCommentOrBr property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTextOrCommentOrBr().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Text }
     * {@link String }
     * {@link Br }
     * {@link Hr }
     * 
     * 
     */
    public List<Object> getTextOrCommentOrBr() {
        if (textOrCommentOrBr == null) {
            textOrCommentOrBr = new ArrayList<Object>();
        }
        return this.textOrCommentOrBr;
    }

    /**
     * Gets the value of the time property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the time property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTime().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Time }
     * 
     * 
     */
    public List<Time> getTime() {
        if (time == null) {
            time = new ArrayList<Time>();
        }
        return this.time;
    }

}
