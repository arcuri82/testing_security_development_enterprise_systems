
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
 *       &lt;sequence>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{}subtitle"/>
 *           &lt;element ref="{}text"/>
 *           &lt;element ref="{}comment"/>
 *           &lt;element ref="{}br"/>
 *           &lt;element ref="{}hr"/>
 *         &lt;/choice>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element ref="{}location"/>
 *           &lt;element ref="{}repeat"/>
 *           &lt;element ref="{}slocation"/>
 *         &lt;/choice>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{}br"/>
 *           &lt;element ref="{}hr"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "subtitleOrTextOrComment",
    "locationOrRepeatOrSlocation",
    "brOrHr"
})
@XmlRootElement(name = "forecasttype")
public class Forecasttype {

    @XmlElements({
        @XmlElement(name = "subtitle", type = Subtitle.class),
        @XmlElement(name = "text", type = Text.class),
        @XmlElement(name = "comment", type = String.class),
        @XmlElement(name = "br", type = Br.class),
        @XmlElement(name = "hr", type = Hr.class)
    })
    protected List<Object> subtitleOrTextOrComment;
    @XmlElements({
        @XmlElement(name = "location", type = Location.class),
        @XmlElement(name = "repeat", type = Repeat.class),
        @XmlElement(name = "slocation", type = Slocation.class)
    })
    protected List<Object> locationOrRepeatOrSlocation;
    @XmlElements({
        @XmlElement(name = "br", type = Br.class),
        @XmlElement(name = "hr", type = Hr.class)
    })
    protected List<Object> brOrHr;
    @XmlAttribute(name = "name", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String name;

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
     * Gets the value of the locationOrRepeatOrSlocation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the locationOrRepeatOrSlocation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocationOrRepeatOrSlocation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Location }
     * {@link Repeat }
     * {@link Slocation }
     * 
     * 
     */
    public List<Object> getLocationOrRepeatOrSlocation() {
        if (locationOrRepeatOrSlocation == null) {
            locationOrRepeatOrSlocation = new ArrayList<Object>();
        }
        return this.locationOrRepeatOrSlocation;
    }

    /**
     * Gets the value of the brOrHr property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the brOrHr property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBrOrHr().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Br }
     * {@link Hr }
     * 
     * 
     */
    public List<Object> getBrOrHr() {
        if (brOrHr == null) {
            brOrHr = new ArrayList<Object>();
        }
        return this.brOrHr;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
