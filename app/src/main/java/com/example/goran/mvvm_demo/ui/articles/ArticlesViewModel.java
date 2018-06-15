package com.example.goran.mvvm_demo.ui.articles;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.NonNull;

import com.example.goran.mvvm_demo.data.DataRepository;
import com.example.goran.mvvm_demo.data.model.ArticlesResponse;
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

    public LiveData<List<Article>> getArticlesFromDb() {
        return dataRepository.getArticlesFromDb();
    }

    public LiveData<List<Article>> searchApiForArticles(String query) {
        final MutableLiveData<List<Article>> articlesLiveData = new MutableLiveData<>();

        dataRepository.searchApiForArticles(query).enqueue(new Callback<ArticlesResponse>() {
            @Override
            public void onResponse(@NonNull Call<ArticlesResponse> call, @NonNull Response<ArticlesResponse> response) {
                List<Article> articles = response.body().getArticles();
                articlesLiveData.postValue(articles);
            }

            @Override
            public void onFailure(@NonNull Call<ArticlesResponse> call, @NonNull Throwable t) {
                articlesLiveData.postValue(Collections.emptyList());
            }
        });

        return articlesLiveData;
    }

    public LiveData<List<Article>> searchDbByTitle(String query) {
        return dataRepository.searchDbByTitle(query);
    }

    public void clearDb() {
        dataRepository.deleteAll();
    }

    public LiveData<List<Article>> getArticlesFromApi(String source) {
        final MutableLiveData<List<Article>> articlesLiveData = new MutableLiveData<>();

        dataRepository.getArticlesFromApi(source).enqueue(new Callback<ArticlesResponse>() {
            @Override
            public void onResponse(@NonNull Call<ArticlesResponse> call, @NonNull Response<ArticlesResponse> response) {
                List<Article> articles = response.body().getArticles();
                articlesLiveData.postValue(articles);
            }

            @Override
            public void onFailure(@NonNull Call<ArticlesResponse> call, @NonNull Throwable t) {
                articlesLiveData.postValue(Collections.emptyList());
            }
        });

        return articlesLiveData;
    }
}
