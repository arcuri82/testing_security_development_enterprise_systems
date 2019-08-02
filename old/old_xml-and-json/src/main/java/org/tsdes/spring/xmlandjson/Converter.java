package org.tsdes.spring.xmlandjson;



public interface Converter<T> {

    String toXML(T obj);

    T fromXML(String xml);

    String toJSon(T obj);

    T fromJSon(String json);


    /*
        Transforming strings directly from XML to JSON (and vice-versa) would
        be more efficient. But piping together existing methods is very quick
        and easy, and perfectly valid solution when performance is not a major
        constraint.

        Recall that these default methods in this interface could be overwritten
        if necessary
     */


    default String fromJSonToXml(String json){

        T obj = fromJSon(json);
        String xml = toXML(obj);

        return xml;
    }

    default String fromXmlToJSon(String xml){

        T obj = fromXML(xml);
        String json = toJSon(obj);

        return json;
    }
}
