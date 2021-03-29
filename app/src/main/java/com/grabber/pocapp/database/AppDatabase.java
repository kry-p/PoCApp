package com.grabber.pocapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

// 3개의 테이블을 포함(Budget, NearbySites, Stamp)
@Database(entities = {Prop.class}, version = 1, exportSchema = false)
@TypeConverters({RoomTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    // DAO 선언
    public abstract PropDao propDao();

    private static volatile AppDatabase INSTANCE;

    //싱글톤 패턴 (DB는 한 번만 인스턴스화)
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "application_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // 인스턴스화된 DB 제거
    public static void destroyInstance() {
        INSTANCE = null;
    }
}