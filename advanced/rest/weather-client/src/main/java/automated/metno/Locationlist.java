
package automated.metno;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *       &lt;attribute name="location_ids" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "locationlist")
public class Locationlist {

    @XmlAttribute(name = "location_ids")
    @XmlSchemaType(name = "anySimpleType")
    protected String locationIds;
    @XmlAttribute(name = "name")
    @XmlSchemaType(name = "anySimpleType")
    protected String name;

    /**
     * Gets the value of the locationIds property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationIds() {
        return locationIds;
    }

    /**
     * Sets the value of the locationIds property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationIds(String value) {
        this.locationIds = value;
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
