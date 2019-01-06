package com.blackeye.worth.core.sql.bean;

import com.blackeye.worth.utils.DateX;
import com.blackeye.worth.utils.StringX;

import java.util.Date;

public class Caster {

    public static Object valueOf(Class clazz, String value) {

        if (clazz.equals(String.class)) {
            return value;
        }

        if (Date.class.equals(clazz)){
            if (StringX.isEmpty(value))
                return null;
            return DateX.getDateTime(value);
        }

        if(StringX.isEmpty(value)
                || value.equals("null")
                || value.equals("undefined")
                || value.equals("[object Object]")
                || value.equalsIgnoreCase("nan")) return null;
        if (Integer.class.equals(clazz)){
            return Integer.valueOf(value, 10);
        }else if (Long.class.equals(clazz)){
            return Long.valueOf(value, 10);
        }else if (Double.class.equals(clazz)){
            return Double.valueOf(value);
        }else if (Float.class.equals(clazz)){
            return Float.valueOf(value);
        }

        return null;
    }


    public static  <T> T get(Class<T> clz,Object o){
        if(clz.isInstance(o)){
            return clz.cast(o);
        }
        return null;
    }

}
