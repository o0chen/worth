package com.blackeye.worth.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BeanCopyUtil {
    //source中的非空属性复制到target中
    public static <T> void beanCopy(T source, T target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    //source中的非空属性复制到target中，但是忽略指定的属性，也就是说有些属性是不可修改的（个人业务需要）
    public static <T> void beanCopyWithIngore(T source, T target, String... ignoreProperties) {
        String[] pns = getNullAndIgnorePropertyNames(source, ignoreProperties);
        BeanUtils.copyProperties(source, target, pns);
    }

    public static String[] getNullAndIgnorePropertyNames(Object source, String... ignoreProperties) {
        Set<String> emptyNames = getNullPropertyNameSet(source);
        for (String s : ignoreProperties) {
            emptyNames.add(s);
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static String[] getNullPropertyNames(Object source) {
        Set<String> emptyNames = getNullPropertyNameSet(source);
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static Set<String> getNullPropertyNameSet(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        return emptyNames;
    }


    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();


    /**
     * 将json结果集转化为对象
     *
     * @param jsonData json数据
     * @param clazz    对象中的object类型
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            T t = MAPPER.readValue(jsonData, beanType);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将对象转换成json字符串。
     * <p>Title: pojoToJson</p>
     * <p>Description: </p>
     *
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
        try {
            String string = MAPPER.writeValueAsString(data);
            return string;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;

        Object obj = beanClass.newInstance();

        org.apache.commons.beanutils.BeanUtils.populate(obj, map);

        return obj;
    }

    public static Map<?, ?> objectToMap(Object obj) {
        if (obj == null)
            return null;

        return new org.apache.commons.beanutils.BeanMap(obj);
    }


}