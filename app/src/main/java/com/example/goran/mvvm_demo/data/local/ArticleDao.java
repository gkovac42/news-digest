package com.example.goran.mvvm_demo.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.goran.mvvm_demo.data.model.Article;

import java.util.List;

@Dao
public interface ArticleDao {

    @Query("SELECT * FROM article_table ORDER BY :orderBy ASC")
    LiveData<List<Article>> getAll(String orderBy);

    @Query("SELECT * FROM article_table WHERE title LIKE '%' || :query || '%'")
    LiveData<List<Article>> searchByTitle(String query);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Article article);

    @Delete
    void delete(Article article);

    @Query("DELETE from article_table")
    void deleteAll();

}
