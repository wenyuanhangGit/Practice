package com.wyh;

import org.apache.commons.codec.digest.DigestUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.spi.CalendarNameProvider;

/**
 * Hello world!
 */
public class HelloWorld {

    public static void main(String[] args) {
        /*Map<String,String> envMap = System.getenv();

        for(Map.Entry<String, String> env : envMap.entrySet()){
            System.out.println(env.getKey() + " : " + env.getValue());
        }*/

        /*Properties properties = System.getProperties();

        for(Map.Entry<Object,Object> property : properties.entrySet()){
            System.out.println(property.getKey() + " : " + property.getValue());
        }*/

        List<Integer> list = new ArrayList<Integer>(20);

        System.out.println(list.size());


    }

}
