package com.example.goran.mvvm_demo.ui.sources;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.example.goran.mvvm_demo.data.DataRepository;
import com.example.goran.mvvm_demo.data.model.Source;
import com.example.goran.mvvm_demo.data.model.SourcesResponse;
import com.example.goran.mvvm_demo.util.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SourcesViewModel extends AndroidViewModel {

    private DataRepository dataRepository;
    private MutableLiveData<List<Source>> sourcesLiveData;

    public SourcesViewModel(@NonNull Application application) {
        super(application);

        dataRepository = DataRepository.getInstance(application.getApplicationContext());
    }

    public LiveData<List<Source>> getSourcesFromApi() {
        sourcesLiveData = new MutableLiveData<>();

        dataRepository.getSourcesFromApi().enqueue(new Callback<SourcesResponse>() {
            @Override
            public void onResponse(@NonNull Call<SourcesResponse> call, @NonNull Response<SourcesResponse> response) {
                List<Source> sources = response.body().getSources();
                sourcesLiveData.postValue(sources);
            }

            @Override
            public void onFailure(@NonNull Call<SourcesResponse> call, @NonNull Throwable t) {
                sourcesLiveData.postValue(Collections.emptyList());
            }
        });

        return sourcesLiveData;
    }

    public LiveData<List<Source>> getSourcesByCategory(String category) {
        if (category.equals(Category.ALL.toLowerCase())) {
            return sourcesLiveData;

        } else {
            return Transformations.map(sourcesLiveData, input -> {

                List<Source> filteredSources = new ArrayList<>();

                for (Source s : input) {
                    if (s.getCategory().equals(category)) {
                        filteredSources.add(s);
                    }
                }
                return filteredSources;
            });
        }
    }
}
