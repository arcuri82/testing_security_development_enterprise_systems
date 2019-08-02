package org.tsdes.spring.xmlandjson;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class  ConverterCustomJson<T> extends ConverterImpl<T> {

    public ConverterCustomJson(Class<T> type) {
        super(type);
    }

    public ConverterCustomJson(Class<T> type, String schemaLocation) {
        super(type, schemaLocation);
    }


    @Override
    public T fromJSon(String json) {

        /*
            This is quite tricky to implement, as you need to create a parser
            based on the JSON grammar. If you are interested, you can create
            parsers for custom grammars using JavaCC:
            https://javacc.java.net/
         */

        return super.fromJSon(json);
    }


    @Override
    public String toJSon(T obj) {
        StringBuffer buffer = new StringBuffer(1024);

        convertToJsonDirectlyByReflection(buffer, obj);

        return buffer.toString();
    }

    private void convertToJsonDirectlyByReflection(StringBuffer buffer, Object obj){
        /*
            Note: this is very basic, and does not handle all the
            corner cases. It is just to let you understand how unmarshalling
            tools work.

            Note2: this is using reflection, in a similar way that we saw
            for Dependency Injection in PG5100.
         */

        buffer.append("{\n"); //open object definition

        /*
            Get the Field properties in the object, but skip all the ones
             with no value, ie null
         */
        List<Field> fields = Arrays.asList(obj.getClass().getDeclaredFields())
                .stream()
                .filter(f -> {
                    try {
                        f.setAccessible(true);
                        return f.get(obj) != null;
                    } catch (IllegalAccessException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());


        for(int i=0; i<fields.size(); i++){
            Field field = fields.get(i);
            field.setAccessible(true); // avoid issues with private fields

            Object value = null;
            try {
                value = field.get(obj);
            } catch (IllegalAccessException e) {
                return;
            }

            //print the name
            buffer.append("\"" + field.getName() + "\":");

            Class type = field.getType();
            if(type.isPrimitive() || Integer.class.isAssignableFrom(type)){
                /*
                    primitive type? just append it
                 */
                buffer.append(value);
            } else if(type.equals(String.class)){

                //strings have to be inside ""
                buffer.append("\"" + value + "\"");

            } else if(type.equals(Date.class)){
                //"Oct 8, 2016 10:41:28 AM"
                SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
                String date = format.format((Date) value);
                buffer.append("\"" + date + "\"");

            } else if(List.class.isAssignableFrom(type)){

                /*
                   collections (eg array, list) should be inside []
                 */
                List list = (List) value;
                buffer.append("[\n");
                Iterator<Object> iterator = list.iterator();
                while(iterator.hasNext()){
                    //recursion
                    convertToJsonDirectlyByReflection(buffer, iterator.next());
                    if(iterator.hasNext()){
                        buffer.append(",\n");
                    }
                }
                buffer.append("]\n");
            } else {
                throw new IllegalArgumentException("Cannot handle type: "+type);
            }

            if(i != fields.size()-1){
                buffer.append(",");
            }
            buffer.append("\n");
        }

        buffer.append("}\n"); //close object definition
    }
}