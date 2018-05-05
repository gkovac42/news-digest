package com.example.goran.mvvm_demo.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.goran.mvvm_demo.data.DataRepository;
import com.example.goran.mvvm_demo.data.DataRepositoryImpl;
import com.example.goran.mvvm_demo.data.local.ArticleRoomDatabase;
import com.example.goran.mvvm_demo.data.remote.ApiHelper;
import com.example.goran.mvvm_demo.data.remote.model.ApiResponse;
import com.example.goran.mvvm_demo.data.remote.model.Article;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {

    private DataRepository dataRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);

        dataRepository = new DataRepositoryImpl(new ApiHelper(),
                ArticleRoomDatabase.getDatabase(application.getApplicationContext()));
    }

    public void insert(Article article) {
        dataRepository.insert(article);
    }


    public LiveData<List<Article>> getArticlesData() {
        final MutableLiveData<List<Article>> articles = new MutableLiveData<>();

        Single<ApiResponse> observable = dataRepository.getArticlesRemote()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new SingleObserver<ApiResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(ApiResponse apiResponse) {
                articles.setValue(apiResponse.getArticles());
            }

            @Override
            public void onError(Throwable e) {
                Log.e("ERROR", e.getMessage());
            }
        });
        return articles;
    }
}
