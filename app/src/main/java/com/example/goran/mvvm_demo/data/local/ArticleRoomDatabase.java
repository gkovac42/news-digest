package com.example.goran.mvvm_demo.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.goran.mvvm_demo.data.model.Article;

@Database(entities = Article.class, version = 1, exportSchema = false)
public abstract class ArticleRoomDatabase extends RoomDatabase {

    public abstract ArticleDao articleDao();

    private static ArticleRoomDatabase instance;

    public static ArticleRoomDatabase getDatabase(Context context) {
        if (instance == null) {
            synchronized (ArticleRoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            ArticleRoomDatabase.class, "article_db")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return instance;
    }
}
