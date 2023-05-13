package com.example.italyword;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.italyword.model.ForgatableDao;
import com.example.italyword.model.ForgatableWordModel;
import com.example.italyword.model.WordDetailsDao;
import com.example.italyword.model.WordModel;

@Database(entities = {WordModel.class, ForgatableWordModel.class}, version = 9)
public abstract class AppDatabase extends RoomDatabase {

    public abstract WordDetailsDao wordDao();
    public abstract ForgatableDao forgatableDao();
    private static  AppDatabase instance;
    public static  synchronized  AppDatabase getInstance(Context context){
        if(instance ==null){
            instance = Room.databaseBuilder(context,
                    AppDatabase.class,"Worddbss")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
