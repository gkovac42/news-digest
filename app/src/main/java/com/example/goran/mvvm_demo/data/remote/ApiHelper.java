package com.example.goran.mvvm_demo.data.remote;

import com.example.goran.mvvm_demo.data.model.ApiResponse;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiHelper {

    private final static String BASE_URL = "https://newsapi.org/v2/";
    private final static String SOURCES = "hacker-news";
    private final static String API_KEY = "4e02fcfa306546ba9556850eb654e723";

    private NewsApiService apiService;

    public ApiHelper() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        apiService = retrofit.create(NewsApiService.class);
    }

    public Single<ApiResponse> getArticles() {
        return apiService.getArticles(SOURCES, API_KEY);
    }
}
