package org.tsdes.spring.xmlandjson;

import com.google.gson.Gson;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Objects;

public class ConverterImpl<T> implements Converter<T> {

    private final Class<T> type;
    private final String schemaLocation;

    public ConverterImpl(Class<T> type){
        this(type, null);
    }

    public ConverterImpl(Class<T> type, String schemaLocation){
        this.type = Objects.requireNonNull(type);
        this.schemaLocation = schemaLocation;
    }

    @Override
    public String toXML(T obj) {

        try {
            JAXBContext context = JAXBContext.newInstance(type);

            Marshaller m = context.createMarshaller();
            if(schemaLocation != null) {
                SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

                StreamSource source = new StreamSource(getClass().getResourceAsStream(schemaLocation));
                Schema schema = schemaFactory.newSchema(source);
                m.setSchema(schema);
            }

            StringWriter writer = new StringWriter();

            m.marshal(obj, writer);
            String xml = writer.toString();

            return xml;
        } catch (Exception e) {
            System.out.println("ERROR: "+e.toString());
            return null;
        }
    }

    @Override
    public T fromXML(String xml) {

        try {
            JAXBContext context = JAXBContext.newInstance(type);
            Unmarshaller u = context.createUnmarshaller();

            if(schemaLocation != null) {
                SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

                StreamSource source = new StreamSource(getClass().getResourceAsStream(schemaLocation));
                Schema schema = schemaFactory.newSchema(source);
                u.setSchema(schema);
            }

            StringReader reader = new StringReader(xml);
            T obj = (T) u.unmarshal(reader);

            return obj;
        } catch (Exception e) {
            System.out.println("ERROR: "+e.toString());
            return null;
        }
    }

    @Override
    public String toJSon(T obj) {

        //see https://github.com/google/gson

        Gson gson = new Gson();
        String json = gson.toJson(obj);

        return json;
    }

    @Override
    public T fromJSon(String json) {

        Gson gson = new Gson();
        T obj = gson.fromJson(json, type);

        return obj;
    }
}
