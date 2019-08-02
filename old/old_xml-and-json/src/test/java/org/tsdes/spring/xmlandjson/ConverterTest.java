package org.tsdes.spring.xmlandjson;

import org.junit.Test;
import org.tsdes.spring.xmlandjson.data.Post;
import org.tsdes.spring.xmlandjson.data.TopPosts;

import java.util.Date;

import static org.junit.Assert.*;

public class ConverterTest {


    @Test
    public void testXml(){

        Converter<TopPosts> converter = new ConverterImpl(TopPosts.class);
        TopPosts topPosts = getTopPosts();

        String xml = converter.toXML(topPosts);
        System.out.println(xml);

        TopPosts res = converter.fromXML(xml);

        verifyEquivalence(topPosts,res);
    }

    @Test
    public void testInvalidXml(){

        Converter<TopPosts> converter = new ConverterImpl(TopPosts.class,"/topposts.xsd");
        TopPosts topPosts = getTopPosts();

        String xml = converter.toXML(topPosts);
        assertNotNull(xml);

        topPosts.setRetrievedTime(null);//this is a required field

        xml = converter.toXML(topPosts); //should fail, as invalid
        assertNull(xml);
    }

    @Test
    public void testJSon(){

        Converter<TopPosts> converter = new ConverterImpl(TopPosts.class);

        TopPosts topPosts = getTopPosts();

        String json = converter.toJSon(topPosts);
        System.out.println(json);

        TopPosts res = converter.fromJSon(json);

        verifyEquivalence(topPosts,res);
    }

    @Test
    public void testToFromJSonXml(){

        Converter<TopPosts> converter = new ConverterImpl(TopPosts.class);
        TopPosts topPosts = getTopPosts();

        String json = converter.toJSon(topPosts);

        String xml = converter.fromJSonToXml(json);

        json = converter.fromXmlToJSon(xml);

        TopPosts res = converter.fromJSon(json);

        verifyEquivalence(topPosts,res);
    }


    @Test
    public void testSize(){

        Converter<TopPosts> converter = new ConverterImpl(TopPosts.class);
        TopPosts topPosts = getTopPosts();

        String json = converter.toJSon(topPosts);
        String xml  = converter.toXML(topPosts);

        //usually, XML is larger than JSON
        assertTrue(xml.length() > json.length());
    }

    @Test
    public void testAutomatedFiles(){

        Converter<org.tsdes.spring.xmlandjson.automated.TopPosts> automated =
                new ConverterImpl<>(org.tsdes.spring.xmlandjson.automated.TopPosts.class);

        org.tsdes.spring.xmlandjson.automated.TopPosts tp = new org.tsdes.spring.xmlandjson.automated.TopPosts();

        String xml = automated.toXML(tp);

        org.tsdes.spring.xmlandjson.automated.TopPosts res = automated.fromXML(xml);
        assertEquals(tp.getRetrievedTime(), res.getRetrievedTime());
    }


    @Test
    public void testGeneratedFilesXML(){

        Converter<TopPosts> manual = new ConverterImpl(TopPosts.class);
        Converter<org.tsdes.spring.xmlandjson.automated.TopPosts> automated =
                new ConverterImpl<>(org.tsdes.spring.xmlandjson.automated.TopPosts.class);

        TopPosts mTP = getTopPosts();
        String mxml = manual.toXML(mTP);

        org.tsdes.spring.xmlandjson.automated.TopPosts aTP = automated.fromXML(mxml);
        String axml = automated.toXML(aTP);

        TopPosts res = manual.fromXML(axml);

        verifyEquivalence(mTP,res);
    }


    @Test
    public void testGeneratedFilesJSon(){

        Converter<TopPosts> manual = new ConverterImpl(TopPosts.class);
        TopPosts topPosts = getTopPosts();

        String mxml = manual.toXML(topPosts);
        String mjson = manual.toJSon(topPosts);

        Converter<org.tsdes.spring.xmlandjson.automated.TopPosts> automated =
                new ConverterImpl<>(org.tsdes.spring.xmlandjson.automated.TopPosts.class);

        org.tsdes.spring.xmlandjson.automated.TopPosts aTP = automated.fromXML(mxml);
        String aXml = automated.toXML(aTP);
        String aJS = automated.toJSon(aTP);

        assertEquals(mxml, aXml);
        /*
            They might or might not be equal. In this particular case, they
            are not, as the Java classes have different fields
         */
        assertNotEquals(mjson, aJS);
    }


    @Test
    public void testCustomJson(){

        Converter<TopPosts> converter = new ConverterCustomJson<TopPosts>(TopPosts.class);
        TopPosts topPosts = getTopPosts();

        String custom = converter.toJSon(topPosts);
        System.out.println("Custom JSON:\n" + custom);

        TopPosts backWithGson = converter.fromJSon(custom);

        verifyEquivalence(topPosts, backWithGson);
    }



    private void verifyEquivalence(TopPosts a, TopPosts b) {

        assertNotNull(a);
        assertNotNull(b);
        assertEquals(a.getRetrievedTime().toString(),b.getRetrievedTime().toString());
        assertEquals(a.getPosts().size(), b.getPosts().size());

        for(int i=0; i<a.getPosts().size(); i++) {
            assertEquals(a.getPosts().get(i), b.getPosts().get(i));
        }
    }

    private TopPosts getTopPosts(){

        TopPosts topPosts = new TopPosts();
        topPosts.setRetrievedTime(new Date());

        Post a = new Post();
        a.setAuthor("First");
        a.setContent("A post");
        a.setVotes(2);
        topPosts.addPost(a);

        Post b = new Post();
        b.setContent("A second post with no attributes");
        topPosts.addPost(b);

        return topPosts;
    }
}