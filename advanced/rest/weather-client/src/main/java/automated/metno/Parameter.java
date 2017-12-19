
package automated.metno;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
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
 *         &lt;element ref="{}text" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}comment" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}br" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}in"/>
 *         &lt;element ref="{}text" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}br" maxOccurs="unbounded" minOccurs="0"/>
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
    "content"
})
@XmlRootElement(name = "parameter")
public class Parameter {

    @XmlElementRefs({
        @XmlElementRef(name = "text", type = Text.class, required = false),
        @XmlElementRef(name = "br", type = Br.class, required = false),
        @XmlElementRef(name = "in", type = In.class, required = false),
        @XmlElementRef(name = "comment", type = JAXBElement.class, required = false)
    })
    protected List<Object> content;
    @XmlAttribute(name = "name", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String name;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "Text" is used by two different parts of a schema. See: 
     * line 221 of file:/E:/WORK/code/teaching/testing_security_development_enterprise_systems/spring/rest/weather-client/src/main/resources/textforecast.xsd
     * line 217 of file:/E:/WORK/code/teaching/testing_security_development_enterprise_systems/spring/rest/weather-client/src/main/resources/textforecast.xsd
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names: 
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link Text }
     * {@link Br }
     * {@link In }
     * 
     * 
     */
    public List<Object> getContent() {
        if (content == null) {
            content = new ArrayList<Object>();
        }
        return this.content;
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
