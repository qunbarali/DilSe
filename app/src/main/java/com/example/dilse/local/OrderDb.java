package com.example.dilse.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Order.class}, version = 1)
public abstract class OrderDb extends RoomDatabase {

    public abstract OrderDao orderDao();

    private static OrderDb INSTANCE;

    public static OrderDb getDbInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), OrderDb.class, "MY_ORDERS")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
