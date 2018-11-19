package com.blackeye.worth.utils;

import java.lang.reflect.Field;

public class ReflectUtil {//TODO　后续增加缓存提升性能
    /**
     * 通过反射获取属性值
     */
    public static Object get(Object object, String propertyName) {
        if (object != null && propertyName != null) {
            return get(object, object.getClass(), propertyName);
        }
        return null;
    }
    public static Object get(Object object,Class clazz, String propertyName) {
        if (object != null&&clazz!=null && propertyName != null&&clazz!=Object.class) {
            if(propertyName.contains(".")){
                int firstPosition=propertyName.indexOf(".");
                String  firstField=propertyName.substring(0,firstPosition);
                Object nextObj = get(object,clazz, firstField);
                String nextPropertyName=propertyName.substring(firstPosition+1);
                Class nextClass=nextObj.getClass();//TODO
                return get(nextObj, nextClass, nextPropertyName);
            }else {
                try {
                    Field field = clazz.getDeclaredField(propertyName);
                    field.setAccessible(true);
                    return field.get(object);
                } catch (NoSuchFieldException e) {
                    return get(object, clazz.getSuperclass(), propertyName);
                }catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }
    /**
     * 通过反射设置属性值
     */
    public static boolean set(Object object, String propertyName,Object propertyValue) {
        if (object != null && propertyName != null) {
            return set(object, object.getClass(), propertyName,propertyValue);
        }
        return false;
    }
    public static boolean set(Object object,Class clazz, String propertyName,Object propertyValue) {
        if (object != null&&clazz!=null && propertyName != null&&clazz!=Object.class) {
            try {
                Field field = clazz.getDeclaredField(propertyName);
                field.setAccessible(true);
                field.set(object, propertyValue);
                return true;
            } catch (NoSuchFieldException e) {
                return set(object, clazz.getSuperclass(), propertyName,propertyValue);
            }catch (Exception e) {
                return false;
            }
        }
        return false;
    }

}
