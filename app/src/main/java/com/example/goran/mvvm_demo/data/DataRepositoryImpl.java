package com.example.goran.mvvm_demo.data;

import android.arch.lifecycle.LiveData;
import android.database.sqlite.SQLiteConstraintException;

import com.example.goran.mvvm_demo.data.local.ArticleRoomDatabase;
import com.example.goran.mvvm_demo.data.remote.ApiHelper;
import com.example.goran.mvvm_demo.data.remote.model.ApiResponse;
import com.example.goran.mvvm_demo.data.remote.model.Article;

import java.util.List;

import io.reactivex.Single;

public class DataRepositoryImpl implements DataRepository {

    private ApiHelper apiHelper;
    private ArticleRoomDatabase database;

    public DataRepositoryImpl(ApiHelper apiHelper, ArticleRoomDatabase database) {
        this.apiHelper = apiHelper;
        this.database = database;
    }

    @Override
    public Single<ApiResponse> getArticlesRemote() {
        return apiHelper.getArticles();
    }

    @Override
    public LiveData<List<Article>> getArticlesLocal() {
        return database.articleDao().getArticlesByTitle();
    }

    @Override
    public void insert(Article article) throws SQLiteConstraintException {
        database.articleDao().insert(article);
    }

    @Override
    public void delete(Article article) {
        database.articleDao().delete(article);
    }

    @Override
    public void deleteAll() {
        database.articleDao().deleteAll();
    }
}
