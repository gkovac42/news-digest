package com.example.goran.mvvm_demo.data;

import android.arch.lifecycle.LiveData;

import com.example.goran.mvvm_demo.data.remote.model.ApiResponse;
import com.example.goran.mvvm_demo.data.remote.model.Article;

import java.util.List;

import io.reactivex.Single;

public interface DataRepository {

    Single<ApiResponse> getArticlesRemote();

    LiveData<List<Article>> getArticlesLocal();

    void insert(Article article);

    void delete(Article article);

    void deleteAll();

}
