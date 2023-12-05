package com.volvo.congestiontax.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util {

    public  static LocalDateTime GetLocalDateTimeFromString(String date){
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date,dateTimeFormatter);

    }
}
