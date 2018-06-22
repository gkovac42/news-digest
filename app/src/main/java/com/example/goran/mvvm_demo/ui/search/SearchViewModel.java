package com.example.goran.mvvm_demo.ui.search;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.NonNull;

import com.example.goran.mvvm_demo.data.DataRepository;
import com.example.goran.mvvm_demo.data.model.Article;
import com.example.goran.mvvm_demo.data.model.ArticlesResponse;
import com.example.goran.mvvm_demo.util.Code;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends AndroidViewModel {

    private DataRepository dataRepository;
    private MutableLiveData<List<Article>> articlesLiveData;
    private MutableLiveData<Integer> errorCodeLiveData;

    public SearchViewModel(@NonNull Application application) {
        super(application);

        dataRepository = DataRepository.getInstance(application.getApplicationContext());
        articlesLiveData = new MutableLiveData<>();
        errorCodeLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Article>> getArticlesLiveData() {
        return articlesLiveData;
    }

    public MutableLiveData<Integer> getErrorCodeLiveData() {
        return errorCodeLiveData;
    }

    public void insertIntoDb(Article article) throws SQLiteConstraintException {
        dataRepository.insertIntoDb(article);
    }

    public void deleteFromDb(Article article) {
        dataRepository.deleteFromDb(article);
    }

    public void searchApiForArticles(String query) {
        dataRepository.searchApiForArticles(query).enqueue(new Callback<ArticlesResponse>() {
            @Override
            public void onResponse(@NonNull Call<ArticlesResponse> call,
                                   @NonNull Response<ArticlesResponse> response) {
                List<Article> articles = response.body().getArticles();
                articlesLiveData.postValue(articles);
            }

            @Override
            public void onFailure(@NonNull Call<ArticlesResponse> call,
                                  @NonNull Throwable t) {
                errorCodeLiveData.postValue(Code.NETWORK_ERROR);
            }
        });
    }
}
