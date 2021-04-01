package com.grabber.pocapp.database.pojo;

public class CategoryProp {

    private String category;
    private long sum_amount;
    private int year;
    private int month;

    // 생성자
    public CategoryProp(String category, long sum_amount, int year, int month) {
        this.category = category;
        this.sum_amount = sum_amount;
        this.year = year;
        this.month = month;
    }

    // getter, setter
    public String getCategory() {
        return category;
    }

    public long getAmount() {
        return sum_amount;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

//    public void setCategory(String in){
//        this.category = in;
//    }
//    public void setAmount(long value){
//        this.sum_amount = value;
//    }
}