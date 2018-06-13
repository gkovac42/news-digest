package com.example.goran.mvvm_demo.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.NonNull;

import com.example.goran.mvvm_demo.data.DataRepository;
import com.example.goran.mvvm_demo.data.model.ApiResponse;
import com.example.goran.mvvm_demo.data.model.Article;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArticlesViewModel extends AndroidViewModel {

    private DataRepository dataRepository;

    public ArticlesViewModel(@NonNull Application application) {
        super(application);

        dataRepository = DataRepository.getInstance(application.getApplicationContext());
    }

    public void insertIntoDb(Article article) throws SQLiteConstraintException {
        dataRepository.insertIntoDb(article);
    }

    public void deleteFromDb(Article article) {
        dataRepository.deleteFromDb(article);
    }

    public LiveData<List<Article>> getArticlesFromWeb() {
        final MutableLiveData<List<Article>> articles = new MutableLiveData<>();

        dataRepository.getArticlesFromApi().enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                List<Article> downloadedArticles = response.body().getArticles();
                articles.postValue(downloadedArticles);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
                articles.postValue(Collections.emptyList());
            }
        });

        return articles;
    }

    public LiveData<List<Article>> getArticlesFromDb() {
        return dataRepository.getArticlesFromDb();
    }

    public LiveData<List<Article>> searchArticlesByTitle(String query) {
        return dataRepository.searchDbByTitle(query);
    }

    public void clearDb() {
        dataRepository.deleteAll();
    }
}
