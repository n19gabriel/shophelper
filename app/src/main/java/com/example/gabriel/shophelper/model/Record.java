package com.example.gabriel.shophelper.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gabriel on 24.03.18.
 */

public class Record {
    String time;
    String record;

    public Record() {}

    public Record(String record){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:dd:MM:yyyy");
        this.time = simpleDateFormat.format(calendar.getTime()).toString();
        this.record = record;
    }

    public String getTime() {
        return time;
    }

    public String getRecord() {
        return record;
    }
}
