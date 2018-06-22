package com.example.goran.mvvm_demo.data;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import com.example.goran.mvvm_demo.data.local.ArticleRoomDatabase;
import com.example.goran.mvvm_demo.data.model.Article;
import com.example.goran.mvvm_demo.data.model.ArticlesResponse;
import com.example.goran.mvvm_demo.data.model.SourcesResponse;
import com.example.goran.mvvm_demo.data.remote.ApiHelper;

import java.util.List;

import retrofit2.Call;

public class DataRepository {

    private ApiHelper apiHelper;
    private ArticleRoomDatabase database;
    private static DataRepository instance;

    private DataRepository(ApiHelper apiHelper, ArticleRoomDatabase database) {
        this.apiHelper = apiHelper;
        this.database = database;
    }

    public static DataRepository getInstance(Context context) {
        if (instance == null) {
            instance = new DataRepository(new ApiHelper(context),
                    ArticleRoomDatabase.getDatabase(context));
        }
        return instance;
    }

    public Call<SourcesResponse> getSourcesFromApi() {
        return apiHelper.getSources();
    }

    public Call<ArticlesResponse> getArticlesFromApi(String source) {
        return apiHelper.getArticles(source);
    }

    public Call<ArticlesResponse> searchApiForArticles(String query) {
        return apiHelper.searchArticles(query);
    }

    public LiveData<List<Article>> getArticlesFromDb() {
        return database.articleDao().getAll("title");
    }

    public LiveData<List<Article>> searchDbByTitle(String query) {
        return database.articleDao().searchByTitle(query);
    }

    public void insertIntoDb(Article article) throws SQLiteConstraintException {
        database.articleDao().insert(article);
    }

    public void deleteFromDb(Article article) {
        database.articleDao().delete(article);
    }

    public void deleteAll() {
        database.articleDao().deleteAll();
    }
}
