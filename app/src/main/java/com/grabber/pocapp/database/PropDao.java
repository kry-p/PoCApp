package com.grabber.pocapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PropDao {
    @Insert
    void insert(Prop prop);

    @Update
    void update(Prop prop);

    @Delete
    void delete(Prop prop);

    // 쿼리

    // 모든 내용 가져오기(초기)
    @Query("SELECT * FROM prop")
    LiveData<List<Prop>> getAll();

    // 카테고리에 따라 가져오기
    @Query("SELECT SUM(amount) as sum_amount, category FROM prop GROUP BY category")
    List<CategoryProp> getAllCategory();

    // 날짜에 따라 모든 내용을 조회 (오름차순)
    @Query("SELECT * FROM prop ORDER BY date ASC")
    LiveData<List<Prop>> getAllDateAsc();

    // 날짜에 따라 모든 내용을 조회 (내림차순)
    @Query("SELECT * FROM prop ORDER BY date DESC")
    LiveData<List<Prop>> getAllDateDesc();

    // 입력한 단어를 조회
    @Query("SELECT * FROM prop WHERE category=:str")
    LiveData<List<Prop>> getSearchData(String str);

    // 잔액을 구함
    @Query("SELECT SUM(amount) FROM prop")
    LiveData<Integer> getBalance();

    // 모두 삭제
    @Query("DELETE FROM prop")
    void deleteAll();

}