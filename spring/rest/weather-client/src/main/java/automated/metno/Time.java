
package automated.metno;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{}subtitle"/>
 *           &lt;element ref="{}text"/>
 *           &lt;element ref="{}comment"/>
 *           &lt;element ref="{}br"/>
 *           &lt;element ref="{}hr"/>
 *         &lt;/choice>
 *         &lt;element ref="{}forecasttype"/>
 *       &lt;/sequence>
 *       &lt;attribute name="vfrom" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="vto" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "subtitleOrTextOrComment"
})
@XmlRootElement(name = "time")
public class Time {

    @XmlElements({
        @XmlElement(name = "subtitle", required = true, type = Subtitle.class),
        @XmlElement(name = "text", required = true, type = Text.class),
        @XmlElement(name = "comment", required = true, type = String.class),
        @XmlElement(name = "br", required = true, type = Br.class),
        @XmlElement(name = "hr", required = true, type = Hr.class),
        @XmlElement(name = "forecasttype", required = true, type = Forecasttype.class)
    })
    protected List<Object> subtitleOrTextOrComment;
    @XmlAttribute(name = "vfrom", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String vfrom;
    @XmlAttribute(name = "vto", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String vto;

    /**
     * Gets the value of the subtitleOrTextOrComment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subtitleOrTextOrComment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubtitleOrTextOrComment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Subtitle }
     * {@link Text }
     * {@link String }
     * {@link Br }
     * {@link Hr }
     * {@link Forecasttype }
     * 
     * 
     */
    public List<Object> getSubtitleOrTextOrComment() {
        if (subtitleOrTextOrComment == null) {
            subtitleOrTextOrComment = new ArrayList<Object>();
        }
        return this.subtitleOrTextOrComment;
    }

    /**
     * Gets the value of the vfrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVfrom() {
        return vfrom;
    }

    /**
     * Sets the value of the vfrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVfrom(String value) {
        this.vfrom = value;
    }

    /**
     * Gets the value of the vto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVto() {
        return vto;
    }

    /**
     * Sets the value of the vto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVto(String value) {
        this.vto = value;
    }

}
