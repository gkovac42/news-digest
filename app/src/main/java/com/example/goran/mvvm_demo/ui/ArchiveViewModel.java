package com.example.goran.mvvm_demo.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.goran.mvvm_demo.data.DataRepository;
import com.example.goran.mvvm_demo.data.DataRepositoryImpl;
import com.example.goran.mvvm_demo.data.local.ArticleRoomDatabase;
import com.example.goran.mvvm_demo.data.remote.ApiHelper;
import com.example.goran.mvvm_demo.data.remote.model.Article;

import java.util.List;

public class ArchiveViewModel extends AndroidViewModel {

    private DataRepository dataRepository;
    private LiveData<List<Article>> articles;

    public ArchiveViewModel(@NonNull Application application) {
        super(application);

        dataRepository = new DataRepositoryImpl(new ApiHelper(),
                ArticleRoomDatabase.getDatabase(application.getApplicationContext()));

        articles = dataRepository.getArticlesLocal();
    }

    public LiveData<List<Article>> getArticles() {
        return articles;
    }

    public void delete(Article article) {
        dataRepository.delete(article);
    }
}
