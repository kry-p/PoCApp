package com.grabber.pocapp.function;

import java.util.Calendar;
import java.util.Date;

public class DateToSqlDate {

    private java.util.Date today;

    public java.sql.Date calc(Date date){
        Calendar temp=Calendar.getInstance();
        java.util.Date utilDate;
        temp.setTime(date);

        temp.set(Calendar.HOUR_OF_DAY,0);
        temp.set(Calendar.MINUTE,0);
        temp.set(Calendar.SECOND,0);
        temp.set(Calendar.MILLISECOND,0);

        utilDate=new java.util.Date(temp.getTimeInMillis());
        today.setTime(temp.getTimeInMillis());
        return new java.sql.Date(utilDate.getTime());
    }
}