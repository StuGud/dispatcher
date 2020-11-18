package com.stugud.dispatcher.config;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Created By Gud on 2020/11/13 10:30 下午
 */
class DateConverterConfigTest {

    @Test
    void convert() {
        DateConverterConfig dateConverterConfig = new DateConverterConfig();
        Date date = dateConverterConfig.convert("2020.10.11");
        Date date1 = dateConverterConfig.convert("2020/10/11");
        System.out.println(date1);
    }
}