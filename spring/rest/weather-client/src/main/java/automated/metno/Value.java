
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
 *       &lt;attribute name="src" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="par" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="pos" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="run" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="vfrom" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="vto" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="format" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="lang" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="symbolset" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "value")
public class Value {

    @XmlAttribute(name = "src", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String src;
    @XmlAttribute(name = "par", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String par;
    @XmlAttribute(name = "pos", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String pos;
    @XmlAttribute(name = "run")
    @XmlSchemaType(name = "anySimpleType")
    protected String run;
    @XmlAttribute(name = "vfrom")
    @XmlSchemaType(name = "anySimpleType")
    protected String vfrom;
    @XmlAttribute(name = "vto")
    @XmlSchemaType(name = "anySimpleType")
    protected String vto;
    @XmlAttribute(name = "format")
    @XmlSchemaType(name = "anySimpleType")
    protected String format;
    @XmlAttribute(name = "lang")
    @XmlSchemaType(name = "anySimpleType")
    protected String lang;
    @XmlAttribute(name = "symbolset")
    @XmlSchemaType(name = "anySimpleType")
    protected String symbolset;

    /**
     * Gets the value of the src property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSrc() {
        return src;
    }

    /**
     * Sets the value of the src property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSrc(String value) {
        this.src = value;
    }

    /**
     * Gets the value of the par property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPar() {
        return par;
    }

    /**
     * Sets the value of the par property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPar(String value) {
        this.par = value;
    }

    /**
     * Gets the value of the pos property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPos() {
        return pos;
    }

    /**
     * Sets the value of the pos property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPos(String value) {
        this.pos = value;
    }

    /**
     * Gets the value of the run property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRun() {
        return run;
    }

    /**
     * Sets the value of the run property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRun(String value) {
        this.run = value;
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

    /**
     * Gets the value of the format property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the value of the format property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormat(String value) {
        this.format = value;
    }

    /**
     * Gets the value of the lang property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLang() {
        return lang;
    }

    /**
     * Sets the value of the lang property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLang(String value) {
        this.lang = value;
    }

    /**
     * Gets the value of the symbolset property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSymbolset() {
        return symbolset;
    }

    /**
     * Sets the value of the symbolset property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSymbolset(String value) {
        this.symbolset = value;
    }

}
