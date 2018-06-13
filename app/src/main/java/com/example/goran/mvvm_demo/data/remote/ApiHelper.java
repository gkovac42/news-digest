package com.example.goran.mvvm_demo.data.remote;

import com.example.goran.mvvm_demo.data.model.ApiResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiHelper {

    private static final String BASE_URL = "https://newsapi.org/v2/";
    private static final String API_KEY = "4e02fcfa306546ba9556850eb654e723";

    private final static String SOURCE_HACKER_NEWS = "hacker-news";

    private static final String SORT_BY_POPULARITY = "popularity";
    private static final String SORT_BY_RELEVANCY = "relevancy";
    private static final String SORT_BY_DATE = "publishedAt";

    private NewsApiService apiService;

    public ApiHelper() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(NewsApiService.class);
    }

    public Call<ApiResponse> getArticles() {
        return apiService.getArticles(SOURCE_HACKER_NEWS, API_KEY);
    }
}
