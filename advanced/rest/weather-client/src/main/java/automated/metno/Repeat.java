
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
 *         &lt;element ref="{}location"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{}br"/>
 *           &lt;element ref="{}hr"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="loop" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="parent_id" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="forecast" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="input" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "location",
    "brOrHr"
})
@XmlRootElement(name = "repeat")
public class Repeat {

    @XmlElement(required = true)
    protected Location location;
    @XmlElements({
        @XmlElement(name = "br", type = Br.class),
        @XmlElement(name = "hr", type = Hr.class)
    })
    protected List<Object> brOrHr;
    @XmlAttribute(name = "loop", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String loop;
    @XmlAttribute(name = "type")
    @XmlSchemaType(name = "anySimpleType")
    protected String type;
    @XmlAttribute(name = "parent_id")
    @XmlSchemaType(name = "anySimpleType")
    protected String parentId;
    @XmlAttribute(name = "forecast")
    @XmlSchemaType(name = "anySimpleType")
    protected String forecast;
    @XmlAttribute(name = "input")
    @XmlSchemaType(name = "anySimpleType")
    protected String input;
    @XmlAttribute(name = "name")
    @XmlSchemaType(name = "anySimpleType")
    protected String name;

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setLocation(Location value) {
        this.location = value;
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
     * Gets the value of the loop property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoop() {
        return loop;
    }

    /**
     * Sets the value of the loop property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoop(String value) {
        this.loop = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the parentId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * Sets the value of the parentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentId(String value) {
        this.parentId = value;
    }

    /**
     * Gets the value of the forecast property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForecast() {
        return forecast;
    }

    /**
     * Sets the value of the forecast property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForecast(String value) {
        this.forecast = value;
    }

    /**
     * Gets the value of the input property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInput() {
        return input;
    }

    /**
     * Sets the value of the input property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInput(String value) {
        this.input = value;
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
