
package automated.metno;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the mypackage package. 
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

    private final static QName _Productdescription_QNAME = new QName("", "productdescription");
    private final static QName _Header_QNAME = new QName("", "header");
    private final static QName _Comment_QNAME = new QName("", "comment");
    private final static QName _Title_QNAME = new QName("", "title");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: mypackage
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Locationlist }
     * 
     */
    public Locationlist createLocationlist() {
        return new Locationlist();
    }

    /**
     * Create an instance of {@link Img }
     * 
     */
    public Img createImg() {
        return new Img();
    }

    /**
     * Create an instance of {@link Br }
     * 
     */
    public Br createBr() {
        return new Br();
    }

    /**
     * Create an instance of {@link Blank }
     * 
     */
    public Blank createBlank() {
        return new Blank();
    }

    /**
     * Create an instance of {@link Hr }
     * 
     */
    public Hr createHr() {
        return new Hr();
    }

    /**
     * Create an instance of {@link Tab }
     * 
     */
    public Tab createTab() {
        return new Tab();
    }

    /**
     * Create an instance of {@link Productheader }
     * 
     */
    public Productheader createProductheader() {
        return new Productheader();
    }

    /**
     * Create an instance of {@link Displaytime }
     * 
     */
    public Displaytime createDisplaytime() {
        return new Displaytime();
    }

    /**
     * Create an instance of {@link Repeat }
     * 
     */
    public Repeat createRepeat() {
        return new Repeat();
    }

    /**
     * Create an instance of {@link Location }
     * 
     */
    public Location createLocation() {
        return new Location();
    }

    /**
     * Create an instance of {@link Parameter }
     * 
     */
    public Parameter createParameter() {
        return new Parameter();
    }

    /**
     * Create an instance of {@link Text }
     * 
     */
    public Text createText() {
        return new Text();
    }

    /**
     * Create an instance of {@link Value }
     * 
     */
    public Value createValue() {
        return new Value();
    }

    /**
     * Create an instance of {@link Variable }
     * 
     */
    public Variable createVariable() {
        return new Variable();
    }

    /**
     * Create an instance of {@link In }
     * 
     */
    public In createIn() {
        return new In();
    }

    /**
     * Create an instance of {@link Forecast }
     * 
     */
    public Forecast createForecast() {
        return new Forecast();
    }

    /**
     * Create an instance of {@link Newitem }
     * 
     */
    public Newitem createNewitem() {
        return new Newitem();
    }

    /**
     * Create an instance of {@link Weather }
     * 
     */
    public Weather createWeather() {
        return new Weather();
    }

    /**
     * Create an instance of {@link Guielement }
     * 
     */
    public Guielement createGuielement() {
        return new Guielement();
    }

    /**
     * Create an instance of {@link Guilist }
     * 
     */
    public Guilist createGuilist() {
        return new Guilist();
    }

    /**
     * Create an instance of {@link Guilistelement }
     * 
     */
    public Guilistelement createGuilistelement() {
        return new Guilistelement();
    }

    /**
     * Create an instance of {@link Time }
     * 
     */
    public Time createTime() {
        return new Time();
    }

    /**
     * Create an instance of {@link Subtitle }
     * 
     */
    public Subtitle createSubtitle() {
        return new Subtitle();
    }

    /**
     * Create an instance of {@link Forecasttype }
     * 
     */
    public Forecasttype createForecasttype() {
        return new Forecasttype();
    }

    /**
     * Create an instance of {@link Slocation }
     * 
     */
    public Slocation createSlocation() {
        return new Slocation();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "productdescription")
    public JAXBElement<String> createProductdescription(String value) {
        return new JAXBElement<String>(_Productdescription_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "header")
    public JAXBElement<String> createHeader(String value) {
        return new JAXBElement<String>(_Header_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "comment")
    public JAXBElement<String> createComment(String value) {
        return new JAXBElement<String>(_Comment_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "title")
    public JAXBElement<String> createTitle(String value) {
        return new JAXBElement<String>(_Title_QNAME, String.class, null, value);
    }

}
