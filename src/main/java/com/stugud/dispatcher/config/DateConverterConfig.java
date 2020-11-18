package com.stugud.dispatcher.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created By Gud on 2020/11/13 10:21 下午
 */
@Component
public class DateConverterConfig implements Converter<String, Date> {
    private static final List<String> formats = new ArrayList<>(4);

    static {
        formats.add("yyyy-MM");
        formats.add("yyyy-MM-dd");
        formats.add("yyyy/MM/dd");
        formats.add("yyyy.MM.dd");
        formats.add("yyyy-MM-dd HH:mm");
        formats.add("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public Date convert(String source) {
        String value = source.trim();
        if ("".equals(value)) {
            return null;
        }
        if (source.matches("^\\d{4}-\\d{1,2}$")) {
            return parseDate(source, formats.get(0));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return parseDate(source, formats.get(1));
        } else if (source.matches("^\\d{4}/\\d{1,2}/\\d{1,2}$")) {
            return parseDate(source, formats.get(2));
        } else if (source.matches("^\\d{4}.\\d{1,2}.\\d{1,2}$")) {
            return parseDate(source, formats.get(3));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, formats.get(4));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, formats.get(5));
        } else {
            throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
        }
    }

    /**
     * 格式化日期
     *
     * @param dateStr String 字符型日期
     * @param format  String 格式
     * @return Date 日期
     */
    public Date parseDate(String dateStr, String format) {
        Date date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {

        }
        return date;
    }
}
