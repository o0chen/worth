package com.blackeye.worth.core.sql.bean;


import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class BeanRequest extends BeanBase {

    MultipartHttpServletRequest request;

    public BeanRequest(MultipartHttpServletRequest request) {
        this.request = request;
    }

    @Override
    public Object get(String name) {
        List values = Arrays.asList(request.getParameterMap().get(name));
        if (values == null || values.size() == 0) {
            return null;
        }

        if (values.size() == 1) {
            return values.get(0);
        }

        StringBuilder s = new StringBuilder();
        for (Object o : values) {
            if (s.length() > 0) {
                s.append(",");
            }
            s.append(o);
        }
        return s.toString();
    }

    @Override
    public boolean contains(String name) {
        return request.getParameterMap().containsKey(name);
    }

    @Override
    public void set(String name, Object object) {
        List l = new ArrayList(1);
        l.add(object);
        String[] lArr = new String[l.size()];
        l.toArray(lArr);
        request.getParameterMap().put(name, lArr);
    }


    @Override
    public Set keySet() {
        return request.getParameterMap().keySet();
    }

    @Override
    public Object get() {
        return request;
    }

}
