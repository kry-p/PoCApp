package com.grabber.pocapp.database.pojo;

public class CategoryProp {

    private String category;
    private long sum_amount;

    // 생성자
    public CategoryProp(String category, long sum_amount){
        this.category = category;
        this.sum_amount = sum_amount;
    }

    // getter, setter
    public String getCategory(){
        return category;
    }
    public long getAmount(){
        return sum_amount;
    }

    public void setCategory(String in){
        this.category = in;
    }
    public void setAmount(long value){
        this.sum_amount = value;
    }
}