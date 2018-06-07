package com.example.goran.mvvm_demo.ui.archive;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.NonNull;

import com.example.goran.mvvm_demo.data.DataRepository;
import com.example.goran.mvvm_demo.data.DataRepositoryImpl;
import com.example.goran.mvvm_demo.data.local.ArticleRoomDatabase;
import com.example.goran.mvvm_demo.data.remote.ApiHelper;
import com.example.goran.mvvm_demo.data.model.Article;

import java.util.List;

public class ArchiveViewModel extends AndroidViewModel {

    private DataRepository dataRepository;

    public ArchiveViewModel(@NonNull Application application) {
        super(application);

        dataRepository = new DataRepositoryImpl(new ApiHelper(),
                ArticleRoomDatabase.getDatabase(application.getApplicationContext()));
    }

    public void insert(Article article) throws SQLiteConstraintException {
        dataRepository.insert(article);
    }

    public void delete(Article article) {
        dataRepository.delete(article);
    }

    public LiveData<List<Article>> getArticlesFromDb() {
        return dataRepository.getArticlesLocal();
    }
}
