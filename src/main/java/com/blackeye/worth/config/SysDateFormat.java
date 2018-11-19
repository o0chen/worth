package com.blackeye.worth.config;

import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SysDateFormat extends SimpleDateFormat {


    private static final List<String> formats = new ArrayList<String>(4);
    private static final List<SimpleDateFormat> formaters = new ArrayList<SimpleDateFormat>(4);

    static {
        formats.add("yyyy-MM");
        formats.add("yyyy-MM-dd");
        formats.add("yyyy-MM-dd hh:mm");
        formats.add("yyyy-MM-dd hh:mm:ss");
        formaters.add(new SimpleDateFormat("yyyy-MM"));
        formaters.add(new SimpleDateFormat("yyyy-MM-dd"));
        formaters.add(new SimpleDateFormat("yyyy-MM-dd hh:mm"));
        formaters.add(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
    }

    public Date convert(String source) {
        String value = source.trim();
        if ("".equals(value)) {
            return null;
        }
        return parseDate(source);
    }

    private Integer getIndex(String source) {
        if (source.matches("^\\d{4}-\\d{1,2}$")) {
            return 0;
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return 1;
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            return 2;
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return 3;
        } else {
            return null;
        }

    }

    public String getFormatText(String source) {
        String value = source.trim();
        if ("".equals(value)) {
            return null;
        }
        Integer idx = getIndex(source);
        if (idx != null) {
            return formats.get(idx);
        } else {
            throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
        }
    }


    public SimpleDateFormat getFormater(String source) {
        String value = source.trim();
        if ("".equals(value)) {
            return null;
        }
        Integer idx = getIndex(source);
        if (idx != null) {
            return formaters.get(idx);
        } else {
            throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
        }
    }

    /**
     * 功能描述：格式化日期
     *
     * @param dateStr String 字符型日期
     * @return Date 日期
     */
    public Date parseDate(String dateStr) {
        if (StringUtils.isEmpty(dateStr))
            return null;
        Date date = null;
        try {
            DateFormat dateFormat = getFormater(dateStr);
            date = (Date) dateFormat.parse(dateStr);
        } catch (Exception e) {
        }
        return date;
    }

    @Override
    public Date parse(String source, ParsePosition pos) {
        return getFormater(source).parse(source,pos);
    }
}

