package com.blackeye.worth.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnotationUtil {//TODO　后续增加缓存提升性能

    public static List<Field> getSpecifyAnnotationFileds(Class clazz, Class annotation) {
        try {
            List<Field> fieldList = new ArrayList<>() ;
            while (clazz != null&& !clazz.equals(Object.class)) {//当父类为null的时候说明到达了最上层的父类(Object类).
                fieldList.addAll(Arrays.asList(clazz .getDeclaredFields()));
                clazz = clazz.getSuperclass(); //得到父类,然后赋给自己
            }
            Stream<Field> result = fieldList.stream().filter(it -> it.getAnnotation(annotation) != null);
            return result.collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




}

