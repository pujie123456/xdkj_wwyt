package com.jib.wwyt.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dateutil {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    ParsePosition pos = new ParsePosition(0);
    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
    //将string转换成时间
    public Date stringToDate(String dateString){
        Date strtodate = formatter.parse(dateString, pos);
        return strtodate;
    }
    //将Date转成String
    public String dateToString(Date date,String index){
        String dateString = formatter.format(date);
        if("1".equals(index)){
            dateString = formatter2.format(date);
        }
        return dateString;
    }
    //将string转成Timestamp
    public Timestamp dateToTimestamp(String dateString) {
        if(!dateString.contains(":")){
            dateString=dateString+" 00:00:00";
        }
        Timestamp time = Timestamp.valueOf(dateString);
        return time;
    }
}
