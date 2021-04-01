package com.grabber.pocapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.grabber.pocapp.database.module.RoomTypeConverter;

@Entity(tableName = "prop")
public class Prop {

    // 자산목록 테이블
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String category;
    private int year;
    private int month;
    private long amount;

    // 생성자
    public Prop(String category, int year, int month, long amount) {
        this.category = category;
        this.year = year;
        this.month = month;
        this.amount = amount;
    }

    // getter, setter
    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public long getAmount() {
        return amount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setAmount(long value) {
        this.amount = value;
    }
}