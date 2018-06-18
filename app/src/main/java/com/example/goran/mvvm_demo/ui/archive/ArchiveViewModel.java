package com.example.goran.mvvm_demo.ui.archive;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.database.sqlite.SQLiteConstraintException;
import android.support.annotation.NonNull;

import com.example.goran.mvvm_demo.data.DataRepository;
import com.example.goran.mvvm_demo.data.model.Article;

import java.util.List;

public class ArchiveViewModel extends AndroidViewModel {

    private DataRepository dataRepository;

    public ArchiveViewModel(@NonNull Application application) {
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

    public LiveData<List<Article>> searchDbByTitle(String query) {
        return dataRepository.searchDbByTitle(query);
    }

    public void clearDb() {
        dataRepository.deleteAll();
    }
}
