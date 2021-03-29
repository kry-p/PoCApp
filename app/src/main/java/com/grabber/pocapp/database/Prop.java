package com.grabber.pocapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.sql.Date;

@Entity(tableName = "prop")
public class Prop {

    // 자산목록 테이블
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String category;
    @TypeConverters({RoomTypeConverter.class})
    private Date date;
    private long amount;

    // 생성자
    public Prop(String category, Date date, long amount){
        this.category = category;
        this.date = date;
        this.amount = amount;
    }

    // getter, setter
    public int getId(){
        return id;
    }
    public String getCategory(){
        return category;
    }
    public Date getDate(){
        return date;
    }
    public long getAmount(){
        return amount;
    }

    public void setId(int id){
        this.id = id;
    }
    public void setCategory(String in){
        this.category = in;
    }
    public void setDate(Date date){
        this.date = date;
    }
    public void setAmount(long value){
        this.amount = value;
    }
}